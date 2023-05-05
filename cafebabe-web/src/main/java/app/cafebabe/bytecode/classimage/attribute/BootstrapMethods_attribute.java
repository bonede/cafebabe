package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;

public class BootstrapMethods_attribute extends attribute_info {
    private int num_bootstrap_methods;
    private bootstrap_method[] bootstrap_methods;

    public BootstrapMethods_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public int getNum_bootstrap_methods() {
        return num_bootstrap_methods;
    }

    public bootstrap_method[] getBootstrap_methods() {
        return bootstrap_methods;
    }

    @Override
    public void read() {
        num_bootstrap_methods = classImage.readu2();
        bootstrap_methods = new bootstrap_method[num_bootstrap_methods];
        for (int i = 0; i < num_bootstrap_methods; i++) {
            bootstrap_method bootstrapMethod = new bootstrap_method();
            bootstrap_methods[i] = bootstrapMethod;
            bootstrapMethod.bootstrap_method_ref = classImage.readu2();
            bootstrapMethod.num_bootstrap_arguments = classImage.readu2();
            bootstrapMethod.bootstrap_arguments = new int[bootstrapMethod.num_bootstrap_arguments];
            for (int k = 0; k < bootstrapMethod.num_bootstrap_arguments; k++) {
                bootstrapMethod.bootstrap_arguments[k] = classImage.readu2();
            }

        }
    }
}
