package getfluxed.fluxedcrystals.util.model;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jared on 8/6/2016.
 */
/*
 * Loader for OBJ models.
 * To enable your mod call instance.addDomain(modid).
 * If you need more control over accepted resources - extend the class, and register a new instance with ModelLoaderRegistry.
 */
public enum OBJColourLoader implements ICustomModelLoader {
    INSTANCE;

    private IResourceManager manager;
    private final Set<String> enabledDomains = new HashSet<String>();
    private final Map<ResourceLocation, OBJColourModel> cache = new HashMap<ResourceLocation, OBJColourModel>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<ResourceLocation, Exception>();

    public void addDomain(String domain) {
        enabledDomains.add(domain.toLowerCase());
        FMLLog.log(Level.INFO, "OBJLoader: Domain %s has been added.", domain.toLowerCase());
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
        cache.clear();
        errors.clear();
    }

    public boolean accepts(ResourceLocation modelLocation) {
        return enabledDomains.contains(modelLocation.getResourceDomain()) && modelLocation.getResourcePath().endsWith(".obj");
    }

    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
        if (!cache.containsKey(file)) {
            IResource resource = null;
            try {
                resource = manager.getResource(file);
            } catch (FileNotFoundException e) {
                if (modelLocation.getResourcePath().startsWith("models/block/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/item/" + file.getResourcePath().substring("models/block/".length())));
                else if (modelLocation.getResourcePath().startsWith("models/item/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath().substring("models/item/".length())));
                else throw e;
            }
            OBJColourModel.Parser parser = new OBJColourModel.Parser(resource, manager);
            OBJColourModel model = null;
            try {
                model = parser.parse();
            } catch (Exception e) {
                errors.put(modelLocation, e);
            } finally {
                cache.put(modelLocation, model);
            }
        }
        OBJColourModel model = cache.get(file);
        if (model == null)
            throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + file, errors.get(modelLocation));
        return model;
    }
}
