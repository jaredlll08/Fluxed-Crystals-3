package getfluxed.fluxedcrystals.util.model;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.core.helpers.Strings;

import javax.vecmath.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Jared on 8/6/2016.
 */
public class OBJColourModel implements IRetexturableModel, IModelCustomData {
    //private Gson GSON = new GsonBuilder().create();
    private OBJColourModel.MaterialLibrary matLib;
    private final ResourceLocation modelLocation;
    private OBJColourModel.CustomData customData;

    public OBJColourModel(OBJColourModel.MaterialLibrary matLib, ResourceLocation modelLocation) {
        this(matLib, modelLocation, new OBJColourModel.CustomData());
    }

    public OBJColourModel(OBJColourModel.MaterialLibrary matLib, ResourceLocation modelLocation, OBJColourModel.CustomData customData) {
        this.matLib = matLib;
        this.modelLocation = modelLocation;
        this.customData = customData;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        Iterator<OBJColourModel.Material> materialIterator = this.matLib.materials.values().iterator();
        List<ResourceLocation> textures = Lists.newArrayList();
        while (materialIterator.hasNext()) {
            OBJColourModel.Material mat = materialIterator.next();
            ResourceLocation textureLoc = new ResourceLocation(mat.getTexture().getPath());
            if (!textures.contains(textureLoc) && !mat.isWhite())
                textures.add(textureLoc);
        }
        return textures;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
        builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);
        TextureAtlasSprite missing = bakedTextureGetter.apply(new ResourceLocation("missingno"));
        for (Map.Entry<String, OBJColourModel.Material> e : matLib.materials.entrySet()) {
            if (e.getValue().getTexture().getTextureLocation().getResourcePath().startsWith("#")) {
                FMLLog.severe("OBJLoader: Unresolved texture '%s' for obj model '%s'", e.getValue().getTexture().getTextureLocation().getResourcePath(), modelLocation);
                builder.put(e.getKey(), missing);
            } else {
                builder.put(e.getKey(), bakedTextureGetter.apply(e.getValue().getTexture().getTextureLocation()));
            }
        }
        builder.put("missingno", missing);
        return new OBJColourModel.OBJBakedModel(this, state, format, builder.build());
    }

    public OBJColourModel.MaterialLibrary getMatLib() {
        return this.matLib;
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        OBJColourModel ret = new OBJColourModel(this.matLib, this.modelLocation, new OBJColourModel.CustomData(this.customData, customData));
        return ret;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        OBJColourModel ret = new OBJColourModel(this.matLib.makeLibWithReplacements(textures), this.modelLocation, this.customData);
        return ret;
    }

    static class CustomData {
        public boolean ambientOcclusion = true;
        public boolean gui3d = true;
        // should be an enum, TODO
        //public boolean modifyUVs = false;
        public boolean flipV = false;

        public CustomData(OBJColourModel.CustomData parent, ImmutableMap<String, String> customData) {
            this.ambientOcclusion = parent.ambientOcclusion;
            this.gui3d = parent.gui3d;
            this.flipV = parent.flipV;
            this.process(customData);
        }

        public CustomData() {
        }

        public void process(ImmutableMap<String, String> customData) {
            for (Map.Entry<String, String> e : customData.entrySet()) {
                if (e.getKey().equals("ambient"))
                    this.ambientOcclusion = Boolean.valueOf(e.getValue());
                else if (e.getKey().equals("gui3d"))
                    this.gui3d = Boolean.valueOf(e.getValue());
                /*else if (e.getKey().equals("modifyUVs"))
                    this.modifyUVs = Boolean.valueOf(e.getValue());*/
                else if (e.getKey().equals("flip-v"))
                    this.flipV = Boolean.valueOf(e.getValue());
            }
        }
    }

    public static class Parser {
        private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
        private static Set<String> unknownObjectCommands = new HashSet<String>();
        public OBJColourModel.MaterialLibrary materialLibrary = new OBJColourModel.MaterialLibrary();
        private IResourceManager manager;
        private InputStreamReader objStream;
        private BufferedReader objReader;
        private ResourceLocation objFrom;

        private List<String> groupList = Lists.newArrayList();
        private List<OBJColourModel.Vertex> vertices = Lists.newArrayList();
        private List<OBJColourModel.Normal> normals = Lists.newArrayList();
        private List<OBJColourModel.TextureCoordinate> texCoords = Lists.newArrayList();

        public Parser(IResource from, IResourceManager manager) throws IOException {
            this.manager = manager;
            this.objFrom = from.getResourceLocation();
            this.objStream = new InputStreamReader(from.getInputStream(), Charsets.UTF_8);
            this.objReader = new BufferedReader(objStream);
        }

        public List<String> getElements() {
            return this.groupList;
        }

        private float[] parseFloats(String[] data) // Helper converting strings to floats
        {
            float[] ret = new float[data.length];
            for (int i = 0; i < data.length; i++)
                ret[i] = Float.parseFloat(data[i]);
            return ret;
        }

        //Partial reading of the OBJ format. Documentation taken from http://paulbourke.net/dataformats/obj/
        public OBJColourModel parse() throws IOException {
            String currentLine = "";
            OBJColourModel.Material material = new OBJColourModel.Material();
            material.setName(OBJColourModel.Material.DEFAULT_NAME);
            int usemtlCounter = 0;
            int lineNum = 0;

            for (; ; ) {
                lineNum++;
                currentLine = objReader.readLine();
                if (currentLine == null) break;
                currentLine.trim();
                if (currentLine.isEmpty() || currentLine.startsWith("#")) continue;

                try {
                    String[] fields = WHITE_SPACE.split(currentLine, 2);
                    String key = fields[0];
                    String data = fields[1];
                    String[] splitData = WHITE_SPACE.split(data);

                    if (key.equalsIgnoreCase("mtllib")) {
                        this.materialLibrary.parseMaterials(manager, data, objFrom);
                    } else if (key.equalsIgnoreCase("usemtl")) {
                        material = this.materialLibrary.materials.get(data);
                        usemtlCounter++;
                    } else if (key.equalsIgnoreCase("v")) // Vertices: x y z [w] - w Defaults to 1.0
                    {
                        float[] coords = parseFloats(splitData);
                        Vector4f pos = new Vector4f(coords[0], coords[1], coords[2], coords.length == 4 ? coords[3] : 1.0F);
                        this.vertices.add(new OBJColourModel.Vertex(pos, material));
                    } else if (key.equalsIgnoreCase("vn")) // Vertex normals: x y z
                    {
                        this.normals.add(new OBJColourModel.Normal(parseFloats(splitData)));
                    } else if (key.equalsIgnoreCase("vt")) // Vertex Textures: u [v] [w] - v/w Defaults to 0
                    {
                        float[] coords = parseFloats(splitData);
                        OBJColourModel.TextureCoordinate texCoord = new OBJColourModel.TextureCoordinate(coords[0],
                                coords.length >= 2 ? coords[1] : 0.0F,
                                coords.length >= 3 ? coords[2] : 0.0F);
                        if (texCoord.u < 0.0f || texCoord.u > 1.0f || texCoord.v < 0.0f || texCoord.v > 1.0f)
                            throw new OBJColourModel.UVsOutOfBoundsException(this.objFrom);
                        this.texCoords.add(texCoord);
                    } else if (key.equalsIgnoreCase("f")) // Face Elements: f v1[/vt1][/vn1] ...
                    {
                        if (splitData.length > 4)
                            FMLLog.warning("OBJColourModel.Parser: found a face ('f') with more than 4 vertices, only the first 4 of these vertices will be rendered!");

                        List<OBJColourModel.Vertex> v = Lists.newArrayListWithCapacity(splitData.length);

                        for (int i = 0; i < splitData.length; i++) {
                            String[] pts = splitData[i].split("/");

                            int vert = Integer.parseInt(pts[0]);
                            Integer texture = pts.length < 2 || Strings.isEmpty(pts[1]) ? null : Integer.parseInt(pts[1]);
                            Integer normal = pts.length < 3 || Strings.isEmpty(pts[2]) ? null : Integer.parseInt(pts[2]);

                            vert = vert < 0 ? this.vertices.size() - 1 : vert - 1;
                            OBJColourModel.Vertex newV = new OBJColourModel.Vertex(new Vector4f(this.vertices.get(vert).getPos()), this.vertices.get(vert).getMaterial());

                            if (texture != null)
                                newV.setTextureCoordinate(this.texCoords.get(texture < 0 ? this.texCoords.size() - 1 : texture - 1));
                            if (normal != null)
                                newV.setNormal(this.normals.get(normal < 0 ? this.normals.size() - 1 : normal - 1));

                            v.add(newV);
                        }

                        OBJColourModel.Vertex[] va = v.toArray(new OBJColourModel.Vertex[v.size()]);

                        OBJColourModel.Face face = new OBJColourModel.Face(va, material.name);
                        if (usemtlCounter < this.vertices.size()) {
                            for (OBJColourModel.Vertex ver : face.getVertices()) {
                                ver.setMaterial(material);
                            }
                        }

                        if (groupList.isEmpty()) {
                            if (this.materialLibrary.getGroups().containsKey(OBJColourModel.Group.DEFAULT_NAME)) {
                                this.materialLibrary.getGroups().get(OBJColourModel.Group.DEFAULT_NAME).addFace(face);
                            } else {
                                OBJColourModel.Group def = new OBJColourModel.Group(OBJColourModel.Group.DEFAULT_NAME, null);
                                def.addFace(face);
                                this.materialLibrary.getGroups().put(OBJColourModel.Group.DEFAULT_NAME, def);
                            }
                        } else {
                            for (String s : groupList) {
                                if (this.materialLibrary.getGroups().containsKey(s)) {
                                    this.materialLibrary.getGroups().get(s).addFace(face);
                                } else {
                                    OBJColourModel.Group e = new OBJColourModel.Group(s, null);
                                    e.addFace(face);
                                    this.materialLibrary.getGroups().put(s, e);
                                }
                            }
                        }
                    } else if (key.equalsIgnoreCase("g") || key.equalsIgnoreCase("o")) {
                        groupList.clear();
                        if (key.equalsIgnoreCase("g")) {
                            String[] splitSpace = data.split(" ");
                            for (String s : splitSpace)
                                groupList.add(s);
                        } else {
                            groupList.add(data);
                        }
                    } else {
                        if (!unknownObjectCommands.contains(key)) {
                            unknownObjectCommands.add(key);
                            FMLLog.info("OBJLoader.Parser: command '%s' (model: '%s') is not currently supported, skipping. Line: %d '%s'", key, objFrom, lineNum, currentLine);
                        }
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException(String.format("OBJLoader.Parser: Exception parsing line #%d: `%s`", lineNum, currentLine), e);
                }
            }

            return new OBJColourModel(this.materialLibrary, this.objFrom);
        }
    }

    public static class MaterialLibrary {
        private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
        private Set<String> unknownMaterialCommands = new HashSet<String>();
        private Map<String, OBJColourModel.Material> materials = new HashMap<String, OBJColourModel.Material>();
        private Map<String, OBJColourModel.Group> groups = new HashMap<String, OBJColourModel.Group>();
        private InputStreamReader mtlStream;
        private BufferedReader mtlReader;

//        private float[] minUVBounds = new float[] {0.0f, 0.0f};
//        private float[] maxUVBounds = new float[] {1.0f, 1.0f};

        public MaterialLibrary() {
            this.groups.put(OBJColourModel.Group.DEFAULT_NAME, new OBJColourModel.Group(OBJColourModel.Group.DEFAULT_NAME, null));
            OBJColourModel.Material def = new OBJColourModel.Material();
            def.setName(OBJColourModel.Material.DEFAULT_NAME);
            this.materials.put(OBJColourModel.Material.DEFAULT_NAME, def);
        }

        public OBJColourModel.MaterialLibrary makeLibWithReplacements(ImmutableMap<String, String> replacements) {
            Map<String, OBJColourModel.Material> mats = new HashMap<String, OBJColourModel.Material>();
            for (Map.Entry<String, OBJColourModel.Material> e : this.materials.entrySet()) {
                // key for the material name, with # added if missing
                String keyMat = e.getKey();
                if (!keyMat.startsWith("#")) keyMat = "#" + keyMat;
                // key for the texture name, with ".png" stripped and # added if missing
                String keyTex = e.getValue().getTexture().getPath();
                if (keyTex.endsWith(".png")) keyTex = keyTex.substring(0, keyTex.length() - ".png".length());
                if (!keyTex.startsWith("#")) keyTex = "#" + keyTex;
                if (replacements.containsKey(keyMat)) {
                    OBJColourModel.Texture currentTexture = e.getValue().texture;
                    OBJColourModel.Texture replacementTexture = new OBJColourModel.Texture(replacements.get(keyMat), currentTexture.position, currentTexture.scale, currentTexture.rotation);
                    OBJColourModel.Material replacementMaterial = new OBJColourModel.Material(e.getValue().color, replacementTexture, e.getValue().name);
                    mats.put(e.getKey(), replacementMaterial);
                } else if (replacements.containsKey(keyTex)) {
                    OBJColourModel.Texture currentTexture = e.getValue().texture;
                    OBJColourModel.Texture replacementTexture = new OBJColourModel.Texture(replacements.get(keyTex), currentTexture.position, currentTexture.scale, currentTexture.rotation);
                    OBJColourModel.Material replacementMaterial = new OBJColourModel.Material(e.getValue().color, replacementTexture, e.getValue().name);
                    mats.put(e.getKey(), replacementMaterial);
                } else {
                    mats.put(e.getKey(), e.getValue());
                }
            }
            OBJColourModel.MaterialLibrary ret = new OBJColourModel.MaterialLibrary();
            ret.unknownMaterialCommands = this.unknownMaterialCommands;
            ret.materials = mats;
            ret.groups = this.groups;
            ret.mtlStream = this.mtlStream;
            ret.mtlReader = this.mtlReader;
//            ret.minUVBounds = this.minUVBounds;
//            ret.maxUVBounds = this.maxUVBounds;
            return ret;
        }

//        public float[] getMinUVBounds()
//        {
//            return this.minUVBounds;
//        }

//        public float[] getMaxUVBounds()
//        {
//            return this.maxUVBounds;
//        }

//        public void setUVBounds(float minU, float maxU, float minV, float maxV)
//        {
//            this.minUVBounds[0] = minU;
//            this.maxUVBounds[0] = maxU;
//            this.minUVBounds[1] = minV;
//            this.maxUVBounds[1] = maxV;
//        }

        public Map<String, OBJColourModel.Group> getGroups() {
            return this.groups;
        }

        public List<OBJColourModel.Group> getGroupsContainingFace(OBJColourModel.Face f) {
            List<OBJColourModel.Group> groupList = Lists.newArrayList();
            for (OBJColourModel.Group g : this.groups.values()) {
                if (g.faces.contains(f)) groupList.add(g);
            }
            return groupList;
        }

        public void changeMaterialColor(String name, int color) {
            Vector4f colorVec = new Vector4f();
            colorVec.w = (color >> 24 & 255) / 255;
            colorVec.x = (color >> 16 & 255) / 255;
            colorVec.y = (color >> 8 & 255) / 255;
            colorVec.z = (color & 255) / 255;
            this.materials.get(name).setColor(colorVec);
        }

        public OBJColourModel.Material getMaterial(String name) {
            return this.materials.get(name);
        }

        public ImmutableList<String> getMaterialNames() {
            return ImmutableList.copyOf(this.materials.keySet());
        }

        public void parseMaterials(IResourceManager manager, String path, ResourceLocation from) throws IOException {
            this.materials.clear();
            boolean hasSetTexture = false;
            boolean hasSetColor = false;
            String domain = from.getResourceDomain();
            if (!path.contains("/"))
                path = from.getResourcePath().substring(0, from.getResourcePath().lastIndexOf("/") + 1) + path;
            mtlStream = new InputStreamReader(manager.getResource(new ResourceLocation(domain, path)).getInputStream(), Charsets.UTF_8);
            mtlReader = new BufferedReader(mtlStream);

            String currentLine = "";
            OBJColourModel.Material material = new OBJColourModel.Material();
            material.setName(OBJColourModel.Material.WHITE_NAME);
            material.setTexture(OBJColourModel.Texture.WHITE);
            this.materials.put(OBJColourModel.Material.WHITE_NAME, material);
            this.materials.put(OBJColourModel.Material.DEFAULT_NAME, new OBJColourModel.Material(OBJColourModel.Texture.WHITE));

            for (; ; ) {
                currentLine = mtlReader.readLine();
                if (currentLine == null) break;
                currentLine.trim();
                if (currentLine.isEmpty() || currentLine.startsWith("#")) continue;

                String[] fields = WHITE_SPACE.split(currentLine, 2);
                String key = fields[0];
                String data = fields[1];

                if (key.equalsIgnoreCase("newmtl")) {
                    hasSetColor = false;
                    hasSetTexture = false;
                    material = new OBJColourModel.Material();
                    material.setName(data);
                    this.materials.put(data, material);
                } else if (key.equalsIgnoreCase("Ka") || key.equalsIgnoreCase("Kd") || key.equalsIgnoreCase("Ks")) {
                    if (key.equalsIgnoreCase("Kd") || !hasSetColor) {
                        String[] rgbStrings = WHITE_SPACE.split(data, 3);
                        Vector4f color = new Vector4f(Float.parseFloat(rgbStrings[0]), Float.parseFloat(rgbStrings[1]), Float.parseFloat(rgbStrings[2]), 1.0f);
                        hasSetColor = true;
                        material.setColor(color);
                    } else {
                        FMLLog.info("OBJColourModel: A color has already been defined for material '%s' in '%s'. The color defined by key '%s' will not be applied!", material.getName(), new ResourceLocation(domain, path).toString(), key);
                    }
                } else if (key.equalsIgnoreCase("map_Ka") || key.equalsIgnoreCase("map_Kd") || key.equalsIgnoreCase("map_Ks")) {
                    if (key.equalsIgnoreCase("map_Kd") || !hasSetTexture) {
                        if (data.contains(" ")) {
                            String[] mapStrings = WHITE_SPACE.split(data);
                            String texturePath = mapStrings[mapStrings.length - 1];
                            OBJColourModel.Texture texture = new OBJColourModel.Texture(texturePath);
                            hasSetTexture = true;
                            material.setTexture(texture);
                        } else {
                            OBJColourModel.Texture texture = new OBJColourModel.Texture(data);
                            hasSetTexture = true;
                            material.setTexture(texture);
                        }
                    } else {
                        FMLLog.info("OBJColourModel: A texture has already been defined for material '%s' in '%s'. The texture defined by key '%s' will not be applied!", material.getName(), new ResourceLocation(domain, path).toString(), key);
                    }
                } else if (key.equalsIgnoreCase("d") || key.equalsIgnoreCase("Tr")) {
                    //d <-optional key here> float[0.0:1.0, 1.0]
                    //Tr r g b OR Tr spectral map file OR Tr xyz r g b (CIEXYZ colorspace)
                    String[] splitData = WHITE_SPACE.split(data);
                    float alpha = Float.parseFloat(splitData[splitData.length - 1]);
                    material.getColor().setW(alpha);
                } else {
                    if (!unknownMaterialCommands.contains(key)) {
                        unknownMaterialCommands.add(key);
                        FMLLog.info("OBJLoader.MaterialLibrary: key '%s' (model: '%s') is not currently supported, skipping", key, new ResourceLocation(domain, path));
                    }
                }
            }
        }
    }

    public static class Material {
        public static final String WHITE_NAME = "OBJColourModel.White.Texture.Name";
        public static final String DEFAULT_NAME = "OBJColourModel.Default.Texture.Name";
        private Vector4f color;
        private OBJColourModel.Texture texture = OBJColourModel.Texture.WHITE;
        private String name = DEFAULT_NAME;

        public Material() {
            this(new Vector4f(1f, 1f, 1f, 1f));
        }

        public Material(Vector4f color) {
            this(color, OBJColourModel.Texture.WHITE, WHITE_NAME);
        }

        public Material(OBJColourModel.Texture texture) {
            this(new Vector4f(1f, 1f, 1f, 1f), texture, DEFAULT_NAME);
        }

        public Material(Vector4f color, OBJColourModel.Texture texture, String name) {
            this.color = color;
            this.texture = texture;
            this.name = name != null ? name : DEFAULT_NAME;
        }

        public void setName(String name) {
            this.name = name != null ? name : DEFAULT_NAME;
        }

        public String getName() {
            return this.name;
        }

        public void setColor(Vector4f color) {
            this.color = color;
        }

        public Vector4f getColor() {
            return this.color;
        }

        public void setTexture(OBJColourModel.Texture texture) {
            this.texture = texture;
        }

        public OBJColourModel.Texture getTexture() {
            return this.texture;
        }

        public boolean isWhite() {
            return this.texture.equals(OBJColourModel.Texture.WHITE);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(String.format("%nMaterial:%n"));
            builder.append(String.format("    Name: %s%n", this.name));
            builder.append(String.format("    Color: %s%n", this.color.toString()));
            builder.append(String.format("    Is White: %b%n", this.isWhite()));
            return builder.toString();
        }
    }

    public static class Texture {
        public static OBJColourModel.Texture WHITE = new OBJColourModel.Texture("builtin/white", new Vector2f(0, 0), new Vector2f(1, 1), 0);
        private String path;
        private Vector2f position;
        private Vector2f scale;
        private float rotation;

        public Texture(String path) {
            this(path, new Vector2f(0, 0), new Vector2f(1, 1), 0);
        }

        public Texture(String path, Vector2f position, Vector2f scale, float rotation) {
            this.path = path;
            this.position = position;
            this.scale = scale;
            this.rotation = rotation;
        }

        public ResourceLocation getTextureLocation() {
            ResourceLocation loc = new ResourceLocation(this.path);
            return loc;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }

        public void setPosition(Vector2f position) {
            this.position = position;
        }

        public Vector2f getPosition() {
            return this.position;
        }

        public void setScale(Vector2f scale) {
            this.scale = scale;
        }

        public Vector2f getScale() {
            return this.scale;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        public float getRotation() {
            return this.rotation;
        }
    }

    public static class Face {
        private OBJColourModel.Vertex[] verts = new OBJColourModel.Vertex[4];
        //        private Normal[] norms = new Normal[4];
//        private TextureCoordinate[] texCoords = new TextureCoordinate[4];
        private String materialName = OBJColourModel.Material.DEFAULT_NAME;
        private boolean isTri = false;

        public Face(OBJColourModel.Vertex[] verts) {
            this(verts, OBJColourModel.Material.DEFAULT_NAME);
        }

        public Face(OBJColourModel.Vertex[] verts, String materialName) {
            this.verts = verts != null && verts.length > 2 ? verts : null;
            setMaterialName(materialName);
            checkData();
        }

//        public Face(Vertex[] verts, Normal[] norms)
//        {
//            this(verts, norms, null);
//        }

//        public Face(Vertex[] verts, TextureCoordinate[] texCoords)
//        {
//            this(verts, null, texCoords);
//        }

//        public Face(Vertex[] verts, Normal[] norms, TextureCoordinate[] texCoords)
//        {
//            this(verts, norms, texCoords, Material.DEFAULT_NAME);
//        }

//        public Face(Vertex[] verts, Normal[] norms, TextureCoordinate[] texCoords, String materialName)
//        {
//            this.verts = verts != null && verts.length > 2 ? verts : null;
//            this.norms = norms != null && norms.length > 2 ? norms : null;
//            this.texCoords = texCoords != null && texCoords.length > 2 ? texCoords : null;
//            setMaterialName(materialName);
//            checkData();
//        }

        private void checkData() {
            if (this.verts != null && this.verts.length == 3) {
                this.isTri = true;
                this.verts = new OBJColourModel.Vertex[]{this.verts[0], this.verts[1], this.verts[2], this.verts[2]};
            }
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName != null && !materialName.isEmpty() ? materialName : this.materialName;
        }

        public String getMaterialName() {
            return this.materialName;
        }

        public boolean isTriangles() {
            return isTri;
        }

        public boolean setVertices(OBJColourModel.Vertex[] verts) {
            if (verts == null) return false;
            else this.verts = verts;
            checkData();
            return true;
        }

        public OBJColourModel.Vertex[] getVertices() {
            return this.verts;
        }

//        public boolean areUVsNormalized()
//        {
//            for (Vertex v : this.verts)
//                if (!v.hasNormalizedUVs())
//                    return false;
//            return true;
//        }

//        public void normalizeUVs(float[] min, float[] max)
//        {
//            if (!this.areUVsNormalized())
//            {
//                for (int i = 0; i < this.verts.length; i++) {
//                    TextureCoordinate texCoord = this.verts[i].getTextureCoordinate();
//                    min[0] = texCoord.u < min[0] ? texCoord.u : min[0];
//                    max[0] = texCoord.u > max[0] ? texCoord.u : max[0];
//                    min[1] = texCoord.v < min[1] ? texCoord.v : min[1];
//                    max[1] = texCoord.v > max[1] ? texCoord.v : max[1];
//                }
//
//                for (Vertex v : this.verts) {
//                    v.texCoord.u = (v.texCoord.u - min[0]) / (max[0] - min[0]);
//                    v.texCoord.v = (v.texCoord.v - min[1]) / (max[1] - max[1]);
//                }
//            }
//        }

        public OBJColourModel.Face bake(TRSRTransformation transform) {
            Matrix4f m = transform.getMatrix();
            Matrix3f mn = null;
            OBJColourModel.Vertex[] vertices = new OBJColourModel.Vertex[verts.length];
//            Normal[] normals = norms != null ? new Normal[norms.length] : null;
//            TextureCoordinate[] textureCoords = texCoords != null ? new TextureCoordinate[texCoords.length] : null;

            for (int i = 0; i < verts.length; i++) {
                OBJColourModel.Vertex v = verts[i];
//                Normal n = norms != null ? norms[i] : null;
//                TextureCoordinate t = texCoords != null ? texCoords[i] : null;

                Vector4f pos = new Vector4f(v.getPos()), newPos = new Vector4f();
                pos.w = 1;
                m.transform(pos, newPos);
                vertices[i] = new OBJColourModel.Vertex(newPos, v.getMaterial());

                if (v.hasNormal()) {
                    if (mn == null) {
                        mn = new Matrix3f();
                        m.getRotationScale(mn);
                        mn.invert();
                        mn.transpose();
                    }
                    Vector3f normal = new Vector3f(v.getNormal().getData()), newNormal = new Vector3f();
                    mn.transform(normal, newNormal);
                    newNormal.normalize();
                    vertices[i].setNormal(new OBJColourModel.Normal(newNormal));
                }

                if (v.hasTextureCoordinate()) vertices[i].setTextureCoordinate(v.getTextureCoordinate());
                else v.setTextureCoordinate(OBJColourModel.TextureCoordinate.getDefaultUVs()[i]);

                //texCoords TODO
//                if (t != null) textureCoords[i] = t;
            }
            return new OBJColourModel.Face(vertices, this.materialName);
        }

        public OBJColourModel.Normal getNormal() {
            Vector3f a = this.verts[2].getPos3();
            a.sub(this.verts[0].getPos3());
            Vector3f b = this.verts[3].getPos3();
            b.sub(this.verts[1].getPos3());
            a.cross(a, b);
            a.normalize();
            return new OBJColourModel.Normal(a);
        }
    }

    public static class Vertex {
        private Vector4f position;
        private OBJColourModel.Normal normal;
        private OBJColourModel.TextureCoordinate texCoord;
        private OBJColourModel.Material material = new OBJColourModel.Material();

        public Vertex(Vector4f position, OBJColourModel.Material material) {
            this.position = position;
            this.material = material;
        }

        public void setPos(Vector4f position) {
            this.position = position;
        }

        public Vector4f getPos() {
            return this.position;
        }

        public Vector3f getPos3() {
            return new Vector3f(this.position.x, this.position.y, this.position.z);
        }

        public boolean hasNormal() {
            return this.normal != null;
        }

        public void setNormal(OBJColourModel.Normal normal) {
            this.normal = normal;
        }

        public OBJColourModel.Normal getNormal() {
            return this.normal;
        }

        public boolean hasTextureCoordinate() {
            return this.texCoord != null;
        }

        public void setTextureCoordinate(OBJColourModel.TextureCoordinate texCoord) {
            this.texCoord = texCoord;
        }

        public OBJColourModel.TextureCoordinate getTextureCoordinate() {
            return this.texCoord;
        }

//        public boolean hasNormalizedUVs()
//        {
//            return this.texCoord.u >= 0.0f && this.texCoord.u <= 1.0f && this.texCoord.v >= 0.0f && this.texCoord.v <= 1.0f;
//        }

        public void setMaterial(OBJColourModel.Material material) {
            this.material = material;
        }

        public OBJColourModel.Material getMaterial() {
            return this.material;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("v:%n"));
            builder.append(String.format("    position: %s %s %s%n", position.x, position.y, position.z));
            builder.append(String.format("    material: %s %s %s %s %s%n", material.getName(), material.getColor().x, material.getColor().y, material.getColor().z, material.getColor().w));
            return builder.toString();
        }
    }

    public static class Normal {
        public float x, y, z;

        public Normal() {
            this(0.0f, 0.0f, 0.0f);
        }

        public Normal(float[] data) {
            this(data[0], data[1], data[2]);
        }

        public Normal(Vector3f vector3f) {
            this(vector3f.x, vector3f.y, vector3f.z);
        }

        public Normal(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3f getData() {
            return new Vector3f(this.x, this.y, this.z);
        }
    }

    public static class TextureCoordinate {
        public float u, v, w;

        public TextureCoordinate() {
            this(0.0f, 0.0f, 1.0f);
        }

        public TextureCoordinate(float[] data) {
            this(data[0], data[1], data[2]);
        }

        public TextureCoordinate(Vector3f data) {
            this(data.x, data.y, data.z);
        }

        public TextureCoordinate(float u, float v, float w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        public Vector3f getData() {
            return new Vector3f(this.u, this.v, this.w);
        }

        public static OBJColourModel.TextureCoordinate[] getDefaultUVs() {
            OBJColourModel.TextureCoordinate[] texCoords = new OBJColourModel.TextureCoordinate[4];
            texCoords[0] = new OBJColourModel.TextureCoordinate(0.0f, 0.0f, 1.0f);
            texCoords[1] = new OBJColourModel.TextureCoordinate(1.0f, 0.0f, 1.0f);
            texCoords[2] = new OBJColourModel.TextureCoordinate(1.0f, 1.0f, 1.0f);
            texCoords[3] = new OBJColourModel.TextureCoordinate(0.0f, 1.0f, 1.0f);
            return texCoords;
        }
    }

    @Deprecated
    public static class Group implements IModelPart {
        public static final String DEFAULT_NAME = "OBJColourModel.Default.Element.Name";
        public static final String ALL = "OBJColourModel.Group.All.Key";
        public static final String ALL_EXCEPT = "OBJColourModel.Group.All.Except.Key";
        private String name = DEFAULT_NAME;
        private LinkedHashSet<OBJColourModel.Face> faces = new LinkedHashSet<OBJColourModel.Face>();
        public float[] minUVBounds = new float[]{0.0f, 0.0f};
        public float[] maxUVBounds = new float[]{1.0f, 1.0f};

//        public float[] minUVBounds = new float[] {0.0f, 0.0f};
//        public float[] maxUVBounds = new float[] {1.0f, 1.0f};

        public Group(String name, LinkedHashSet<OBJColourModel.Face> faces) {
            this.name = name != null ? name : DEFAULT_NAME;
            this.faces = faces == null ? new LinkedHashSet<OBJColourModel.Face>() : faces;
        }

        public LinkedHashSet<OBJColourModel.Face> applyTransform(com.google.common.base.Optional<TRSRTransformation> transform) {
            LinkedHashSet<OBJColourModel.Face> faceSet = new LinkedHashSet<OBJColourModel.Face>();
            for (OBJColourModel.Face f : this.faces) {
//                if (minUVBounds != null && maxUVBounds != null) f.normalizeUVs(minUVBounds, maxUVBounds);
                faceSet.add(f.bake(transform.or(TRSRTransformation.identity())));
            }
            return faceSet;
        }

        public String getName() {
            return this.name;
        }

        public LinkedHashSet<OBJColourModel.Face> getFaces() {
            return this.faces;
        }

        public void setFaces(LinkedHashSet<OBJColourModel.Face> faces) {
            this.faces = faces;
        }

        public void addFace(OBJColourModel.Face face) {
            this.faces.add(face);
        }

        public void addFaces(List<OBJColourModel.Face> faces) {
            this.faces.addAll(faces);
        }
    }

    @Deprecated
    public static class OBJState implements IModelState {
        protected Map<String, Boolean> visibilityMap = Maps.newHashMap();
        public IModelState parent;
        protected OBJColourModel.OBJState.Operation operation = OBJColourModel.OBJState.Operation.SET_TRUE;

        public OBJState(List<String> visibleGroups, boolean visibility) {
            this(visibleGroups, visibility, TRSRTransformation.identity());
        }

        public OBJState(List<String> visibleGroups, boolean visibility, IModelState parent) {
            this.parent = parent;
            for (String s : visibleGroups) this.visibilityMap.put(s, visibility);
        }

        public IModelState getParent(IModelState parent) {
            if (parent == null) return null;
            else if (parent instanceof OBJColourModel.OBJState) return ((OBJColourModel.OBJState) parent).parent;
            return parent;
        }

        public com.google.common.base.Optional<TRSRTransformation> apply(com.google.common.base.Optional<? extends IModelPart> part) {
            if (parent != null) return parent.apply(part);
            return com.google.common.base.Optional.absent();
        }

        public Map<String, Boolean> getVisibilityMap() {
            return this.visibilityMap;
        }

        public List<String> getGroupsWithVisibility(boolean visibility) {
            List<String> ret = Lists.newArrayList();
            for (Map.Entry<String, Boolean> e : this.visibilityMap.entrySet()) {
                if (e.getValue() == visibility) {
                    ret.add(e.getKey());
                }
            }
            return ret;
        }

        public List<String> getGroupNamesFromMap() {
            return Lists.newArrayList(this.visibilityMap.keySet());
        }

        public void changeGroupVisibilities(List<String> names, OBJColourModel.OBJState.Operation operation) {
            if (names == null || names.isEmpty()) return;
            this.operation = operation;
            if (names.get(0).equals(OBJColourModel.Group.ALL)) {
                for (String s : this.visibilityMap.keySet()) {
                    this.visibilityMap.put(s, this.operation.performOperation(this.visibilityMap.get(s)));
                }
            } else if (names.get(0).equals(OBJColourModel.Group.ALL_EXCEPT)) {
                for (String s : this.visibilityMap.keySet()) {
                    if (!names.subList(1, names.size()).contains(s)) {
                        this.visibilityMap.put(s, this.operation.performOperation(this.visibilityMap.get(s)));
                    }
                }
            } else {
                for (String s : names) {
                    this.visibilityMap.put(s, this.operation.performOperation(this.visibilityMap.get(s)));
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("OBJState: ");
            builder.append(String.format("%n    parent: %s%n", this.parent.toString()));
            builder.append(String.format("    visibility map: %n"));
            for (Map.Entry<String, Boolean> e : this.visibilityMap.entrySet()) {
                builder.append(String.format("        name: %s visible: %b%n", e.getKey(), e.getValue()));
            }
            return builder.toString();
        }

        @Override
        public int hashCode() {
            return com.google.common.base.Objects.hashCode(visibilityMap, parent, operation);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            OBJColourModel.OBJState other = (OBJColourModel.OBJState) obj;
            return com.google.common.base.Objects.equal(visibilityMap, other.visibilityMap) &&
                    com.google.common.base.Objects.equal(parent, other.parent) &&
                    operation == other.operation;
        }

        public enum Operation {
            SET_TRUE,
            SET_FALSE,
            TOGGLE;

            Operation() {
            }

            public boolean performOperation(boolean valueToToggle) {
                switch (this) {
                    default:
                    case SET_TRUE:
                        return true;
                    case SET_FALSE:
                        return false;
                    case TOGGLE:
                        return !valueToToggle;
                }
            }
        }
    }

    @Deprecated
    public enum OBJProperty implements IUnlistedProperty<OBJColourModel.OBJState> {
        INSTANCE;

        public String getName() {
            return "OBJProperty";
        }

        @Override
        public boolean isValid(OBJColourModel.OBJState value) {
            return value instanceof OBJColourModel.OBJState;
        }

        @Override
        public Class<OBJColourModel.OBJState> getType() {
            return OBJColourModel.OBJState.class;
        }

        @Override
        public String valueToString(OBJColourModel.OBJState value) {
            return value.toString();
        }
    }

    public class OBJBakedModel implements IPerspectiveAwareModel {
        private final OBJColourModel model;
        private IModelState state;
        private final VertexFormat format;
        private ImmutableList<BakedQuad> quads;
        private ImmutableMap<String, TextureAtlasSprite> textures;
        private TextureAtlasSprite sprite = ModelLoader.White.INSTANCE;

        public OBJBakedModel(OBJColourModel model, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textures) {
            this.model = model;
            this.state = state;
            if (this.state instanceof OBJColourModel.OBJState)
                this.updateStateVisibilityMap((OBJColourModel.OBJState) this.state);
            this.format = format;
            this.textures = textures;
        }

        public void scheduleRebake() {
        }

        // FIXME: merge with getQuads
        @Override
        public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand) {
            if (side != null) return ImmutableList.of();
            if (quads == null) {
                quads = buildQuads(this.state);
            }
            if (blockState instanceof IExtendedBlockState) {
                IExtendedBlockState exState = (IExtendedBlockState) blockState;
                if (exState.getUnlistedNames().contains(net.minecraftforge.common.property.Properties.AnimationProperty)) {

                    IModelState newState = exState.getValue(net.minecraftforge.common.property.Properties.AnimationProperty);
                    if (newState != null) {
                        newState = new ModelStateComposition(this.state, newState);
                        return buildQuads(newState);
                    }
                }
            }
            return quads;
        }

        private ImmutableList<BakedQuad> buildQuads(IModelState modelState) {
            List<BakedQuad> quads = Lists.newArrayList();
            Collections.synchronizedSet(new LinkedHashSet<BakedQuad>());
            Set<OBJColourModel.Face> faces = Collections.synchronizedSet(new LinkedHashSet<OBJColourModel.Face>());
            com.google.common.base.Optional<TRSRTransformation> transform = com.google.common.base.Optional.absent();
            for (OBJColourModel.Group g : this.model.getMatLib().getGroups().values()) {
//                g.minUVBounds = this.model.getMatLib().minUVBounds;
//                g.maxUVBounds = this.model.getMatLib().maxUVBounds;
//                FMLLog.info("Group: %s u: [%f, %f] v: [%f, %f]", g.name, g.minUVBounds[0], g.maxUVBounds[0], g.minUVBounds[1], g.maxUVBounds[1]);

                if (modelState.apply(com.google.common.base.Optional.of(Models.getHiddenModelPart(ImmutableList.of(g.getName())))).isPresent()) {
                    continue;
                }
                if (modelState instanceof OBJColourModel.OBJState) {
                    OBJColourModel.OBJState state = (OBJColourModel.OBJState) modelState;
                    if (state.parent != null) {
                        transform = state.parent.apply(com.google.common.base.Optional.<IModelPart>absent());
                    }
                    //TODO: can this be replaced by updateStateVisibilityMap(OBJState)?
                    if (state.getGroupNamesFromMap().contains(OBJColourModel.Group.ALL)) {
                        state.visibilityMap.clear();
                        for (String s : this.model.getMatLib().getGroups().keySet()) {
                            state.visibilityMap.put(s, state.operation.performOperation(true));
                        }
                    } else if (state.getGroupNamesFromMap().contains(OBJColourModel.Group.ALL_EXCEPT)) {
                        List<String> exceptList = state.getGroupNamesFromMap().subList(1, state.getGroupNamesFromMap().size());
                        state.visibilityMap.clear();
                        for (String s : this.model.getMatLib().getGroups().keySet()) {
                            if (!exceptList.contains(s)) {
                                state.visibilityMap.put(s, state.operation.performOperation(true));
                            }
                        }
                    } else {
                        for (String s : state.visibilityMap.keySet()) {
                            state.visibilityMap.put(s, state.operation.performOperation(state.visibilityMap.get(s)));
                        }
                    }
                    if (state.getGroupsWithVisibility(true).contains(g.getName())) {
                        faces.addAll(g.applyTransform(transform));
                    }
                } else {
                    transform = modelState.apply(com.google.common.base.Optional.<IModelPart>absent());
                    faces.addAll(g.applyTransform(transform));
                }
            }
            for (OBJColourModel.Face f : faces) {
                if (this.model.getMatLib().materials.get(f.getMaterialName()).isWhite()) {
                    for (OBJColourModel.Vertex v : f.getVertices()) {//update material in each vertex
                        if (!v.getMaterial().equals(this.model.getMatLib().getMaterial(v.getMaterial().getName()))) {
                            v.setMaterial(this.model.getMatLib().getMaterial(v.getMaterial().getName()));
                        }
                    }
                    sprite = ModelLoader.White.INSTANCE;
                } else sprite = this.textures.get(f.getMaterialName());
                UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
                builder.setContractUVs(true);
                builder.setQuadOrientation(EnumFacing.getFacingFromVector(f.getNormal().x, f.getNormal().y, f.getNormal().z));
                builder.setTexture(sprite);
                builder.setQuadTint(0);
                OBJColourModel.Normal faceNormal = f.getNormal();
                putVertexData(builder, f.verts[0], faceNormal, OBJColourModel.TextureCoordinate.getDefaultUVs()[0], sprite);
                putVertexData(builder, f.verts[1], faceNormal, OBJColourModel.TextureCoordinate.getDefaultUVs()[1], sprite);
                putVertexData(builder, f.verts[2], faceNormal, OBJColourModel.TextureCoordinate.getDefaultUVs()[2], sprite);
                putVertexData(builder, f.verts[3], faceNormal, OBJColourModel.TextureCoordinate.getDefaultUVs()[3], sprite);
                quads.add(builder.build());
            }
            return ImmutableList.copyOf(quads);
        }

        private final void putVertexData(UnpackedBakedQuad.Builder builder, OBJColourModel.Vertex v, OBJColourModel.Normal faceNormal, OBJColourModel.TextureCoordinate defUV, TextureAtlasSprite sprite) {
            for (int e = 0; e < format.getElementCount(); e++) {
                switch (format.getElement(e).getUsage()) {
                    case POSITION:
                        builder.put(e, v.getPos().x, v.getPos().y, v.getPos().z, v.getPos().w);
                        break;
                    case COLOR:
                        if (v.getMaterial() != null)
                            builder.put(e,
                                    v.getMaterial().getColor().x,
                                    v.getMaterial().getColor().y,
                                    v.getMaterial().getColor().z,
                                    v.getMaterial().getColor().w);
                        else
                            builder.put(e, 1, 1, 1, 1);
                        break;
                    case UV:
                        if (!v.hasTextureCoordinate())
                            builder.put(e,
                                    sprite.getInterpolatedU(defUV.u * 16),
                                    sprite.getInterpolatedV((model.customData.flipV ? 1 - defUV.v : defUV.v) * 16),
                                    0, 1);
                        else
                            builder.put(e,
                                    sprite.getInterpolatedU(v.getTextureCoordinate().u * 16),
                                    sprite.getInterpolatedV((model.customData.flipV ? 1 - v.getTextureCoordinate().v : v.getTextureCoordinate().v) * 16),
                                    0, 1);
                        break;
                    case NORMAL:
                        if (!v.hasNormal())
                            builder.put(e, faceNormal.x, faceNormal.y, faceNormal.z, 0);
                        else
                            builder.put(e, v.getNormal().x, v.getNormal().y, v.getNormal().z, 0);
                        break;
                    default:
                        builder.put(e);
                }
            }
        }

        @Override
        public boolean isAmbientOcclusion() {
            return model != null ? model.customData.ambientOcclusion : true;
        }

        @Override
        public boolean isGui3d() {
            return model != null ? model.customData.gui3d : true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return this.sprite;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        // FIXME: merge with getQuads
        /* @Override
        public OBJBakedModel handleBlockState(IBlockState state)
        {
            if (state instanceof IExtendedBlockState)
            {
                IExtendedBlockState exState = (IExtendedBlockState) state;
                if (exState.getUnlistedNames().contains(OBJProperty.instance))
                {
                    OBJState s = exState.getValue(OBJProperty.instance);
                    if (s != null)
                    {
                        if (s.visibilityMap.containsKey(Group.ALL) || s.visibilityMap.containsKey(Group.ALL_EXCEPT))
                        {
                            this.updateStateVisibilityMap(s);
                        }
                        return getCachedModel(s);
                    }
                }
            }
            return this;
        }*/

        private void updateStateVisibilityMap(OBJColourModel.OBJState state) {
            if (state.visibilityMap.containsKey(OBJColourModel.Group.ALL)) {
                boolean operation = state.visibilityMap.get(OBJColourModel.Group.ALL);
                state.visibilityMap.clear();
                for (String s : this.model.getMatLib().getGroups().keySet()) {
                    state.visibilityMap.put(s, state.operation.performOperation(operation));
                }
            } else if (state.visibilityMap.containsKey(OBJColourModel.Group.ALL_EXCEPT)) {
                List<String> exceptList = state.getGroupNamesFromMap().subList(1, state.getGroupNamesFromMap().size());
                state.visibilityMap.remove(OBJColourModel.Group.ALL_EXCEPT);
                for (String s : this.model.getMatLib().getGroups().keySet()) {
                    if (!exceptList.contains(s)) {
                        state.visibilityMap.put(s, state.operation.performOperation(state.visibilityMap.get(s)));
                    }
                }
            } else {
                for (String s : state.visibilityMap.keySet()) {
                    state.visibilityMap.put(s, state.operation.performOperation(state.visibilityMap.get(s)));
                }
            }
        }

        private final LoadingCache<IModelState, OBJColourModel.OBJBakedModel> cache = CacheBuilder.newBuilder().maximumSize(20).build(new CacheLoader<IModelState, OBJColourModel.OBJBakedModel>() {
            public OBJColourModel.OBJBakedModel load(IModelState state) throws Exception {
                return new OBJColourModel.OBJBakedModel(model, state, format, textures);
            }
        });

        public OBJColourModel.OBJBakedModel getCachedModel(IModelState state) {
            return cache.getUnchecked(state);
        }

        public OBJColourModel getModel() {
            return this.model;
        }

        public IModelState getState() {
            return this.state;
        }

        public OBJColourModel.OBJBakedModel getBakedModel() {
            return new OBJColourModel.OBJBakedModel(this.model, this.state, this.format, this.textures);
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, state, cameraTransformType);
        }

        @Override
        public String toString() {
            return this.model.modelLocation.toString();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }
    }

    @SuppressWarnings("serial")
    public static class UVsOutOfBoundsException extends RuntimeException {
        public ResourceLocation modelLocation;

        public UVsOutOfBoundsException(ResourceLocation modelLocation) {
            super(String.format("Model '%s' has UVs ('vt') out of bounds 0-1! The missing model will be used instead. Support for UV processing will be added to the OBJ loader in the future.", modelLocation));
            this.modelLocation = modelLocation;
        }
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }
}
