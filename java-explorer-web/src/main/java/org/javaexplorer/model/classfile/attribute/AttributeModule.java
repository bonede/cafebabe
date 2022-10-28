package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.flag.ModFlag;

import java.util.List;

/**
 * SPEC
 */
@Data
public class AttributeModule extends AttributeInfo {
    private int moduleNameIndex;
    private String moduleName;
    private List<ModFlag> modFlags;
    private int moduleVersionIndex;
    private String moduleVersion;
    private List<RequireInfo> requires;
    private List<ExportInfo> exports;
    private List<OpenInfo> opens;
    private int[] usesIndices;
    private List<ProvideInfo> provides;
    public AttributeModule(
            int nameIndex,
            String name,
            int length,
            int moduleNameIndex,
            String moduleName,
            List<ModFlag> modFlags,
            int moduleVersionIndex,
            String moduleVersion,
            List<RequireInfo> requires,
            List<ExportInfo> exports,
            List<OpenInfo> opens,
            int[] usesIndices,
            List<ProvideInfo> provides
    ) {
        super(nameIndex, name, length);
        this.moduleNameIndex = moduleNameIndex;
        this.moduleName = moduleName;
        this.modFlags = modFlags;
        this.moduleVersionIndex = moduleVersionIndex;
        this.moduleVersion = moduleVersion;
        this.requires = requires;
        this.moduleNameIndex = moduleNameIndex;
        this.opens = opens;
        this.usesIndices = usesIndices;
        this.provides = provides;
    }
}
