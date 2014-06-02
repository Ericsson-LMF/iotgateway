package com.ericsson.deviceaccess.serviceschema.codegenerator;

import com.ericsson.deviceaccess.service.xmlparser.ActionDocument.Action;
import com.ericsson.deviceaccess.service.xmlparser.ParameterDocument.Parameter;
import com.ericsson.deviceaccess.service.xmlparser.ServiceDocument.Service;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.CodeBlock;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.Param;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.builders.Constructor;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.builders.JavaClass;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.builders.Javadoc;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.builders.Method;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.builders.Variable;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.modifiers.AccessModifier;
import com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder.modifiers.ClassModifier;

/**
 * Adds schema definitions to builder
 *
 * @author delma
 */
public enum DefinitionsAdder {

    /**
     * Singleton
     */
    INSTANCE;

    /**
     * Adds start of SchemaDefinitions to builder
     *
     * @param builder
     * @return block where schema definition happens
     */
    public static CodeBlock addDefinitionsStart(JavaClass builder) {
        builder.setPackage("com.ericsson.deviceaccess.spi.service");
        builder.addImport("java.util.HashMap");
        builder.addImport("java.util.Map");
        builder.addImport("com.ericsson.deviceaccess.spi.schema.ServiceSchema");
        builder.addImport("com.ericsson.deviceaccess.spi.schema.ActionSchema");
        builder.addImport("com.ericsson.deviceaccess.spi.schema.ParameterSchema");
        builder.addImport("com.ericsson.deviceaccess.spi.schema.ServiceSchemaError");
        builder.addImport("java.util.function.Consumer");
        builder.setClassModifier(ClassModifier.SINGLETON);
        builder.setName("SchemaDefinitions");
        builder.setJavadoc(new Javadoc("Defines service schemata"));
        builder.addVariable(new Variable("Map<String, ServiceSchema>", "serviceSchemas").init("new HashMap<>()"));
        Constructor code = new Constructor().setJavadoc(new Javadoc("Constructor which generates schemata."));
        builder.addConstructor(code);
//        code.add("ActionSchema.Builder ").append(ACTION_SB).append(";");
//        code.add("ParameterSchema.Builder ").append(PARAMETER_SB).append(";");
//        code.add("ServiceSchema.Builder ").append(SERVICE_SB).append(";");
        builder.addMethod(new Method("ServiceSchema", "getServiceSchema")
                .setJavadoc(new Javadoc("Gets ServiceSchema based on it's name.").result("Service schema"))
                .addParameter(new Param("String", "name").setDescription("name of schema"))
                .add("return serviceSchemas.get(#0);"));
        builder.addMethod(new Method("void", "addService")
                .setAccessModifier(AccessModifier.PRIVATE)
                .addParameter(new Param("Consumer<ServiceSchema.Builder>", "consumer"))
                .add("ServiceSchema.Builder builder = new ServiceSchema.Builder();")
                .add("#0.accept(builder);")
                .add("ServiceSchema schema = builder.build();")
                .add("serviceSchemas.put(schema.getName(), schema);"));
        return code;
    }

    /**
     * Adds services schema definition
     *
     * @param code block to add schema definition
     * @param service service which schema definition to add
     */
    public static void addService(CodeBlock code, Service service) {
        code.add("");
        code.addBlock("addService(s ->", block -> {
            block.add("s.setName(\"").append(service.getName()).append("\");");
            if (service.isSetActions()) {
                for (Action action : service.getActions().getActionArray()) {
                    block.addBlock("s.addAction(a ->", actionBlock -> {
                        actionBlock.add("a.setName(\"").append(action.getName()).append("\");");
                        if (!action.getOptional()) {
                            actionBlock.add("a.setMandatory(true);");
                        }
                        if (action.isSetArguments()) {
                            addParameters(actionBlock, action.getArguments().getParameterArray(), "a.addArgument");
                        }
                        if (action.isSetResults()) {
                            addParameters(actionBlock, action.getResults().getParameterArray(), "a.addResult");
                        }
                    }).append(");");
                }
            }
            if (service.isSetProperties()) {
                addParameters(block, service.getProperties().getParameterArray(), "s.addProperty");
            }
        }).append(");");
    }

    private static void addParameters(CodeBlock block, Parameter[] parameters, String method) {
        for (Parameter parameter : parameters) {
            block.addBlock(method + "(p ->", parameterBlock -> {
                parameterBlock.add("p.setName(\"").append(parameter.getName()).append("\");");
                parameterBlock.add("p.setType(").append(parameter.getType()).append(".class);");
                String defaultStr = parameter.getDefault();
                if (defaultStr == null && parameter.isSetValues()) {
                    defaultStr = parameter.getValues().getValueArray(0);
                }
                if (defaultStr != null) {
                    parameterBlock.add("p.setDefault(\"").append(defaultStr).append("\");");
                }
                if ("String".equals(StringHelper.getType(parameter.getType()))) {
                    if (parameter.isSetValues()) {
                        parameterBlock.addBlock("p.setValidValues(new String[]", defaultBlock -> {
                            boolean first = true;
                            for (String value : parameter.getValues().getValueArray()) {
                                if (!first) {
                                    defaultBlock.append(",");
                                }
                                first = false;
                                defaultBlock.add("\"").append(value).append('"');
                            }
                        }).append(");");
                    }
                }
                if (parameter.isSetMin()) {
                    parameterBlock.add("p.setMinValue(\"").append(parameter.getMin()).append("\");");
                }
                if (parameter.isSetMax()) {
                    parameterBlock.add("p.setMaxValue(\"").append(parameter.getMax()).append("\");");
                }
            }).append(");");
        }
    }
}
