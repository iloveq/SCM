package com.woaiqw.scm_compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.woaiqw.scm_annotation.annotion.Action;
import com.woaiqw.scm_compiler.model.ActionMeta;
import com.woaiqw.scm_compiler.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.woaiqw.scm_compiler.utils.Constants.ANNOTATION_ACTION_PATH;
import static com.woaiqw.scm_compiler.utils.Constants.FILE_COMMENT;
import static com.woaiqw.scm_compiler.utils.Constants.PACKAGE_OF_GENERATE_FILE;
import static com.woaiqw.scm_compiler.utils.Constants.SCM;


/**
 * Created by haoran on 2018/8/16.
 */

@SupportedOptions(SCM)
@SupportedAnnotationTypes({ANNOTATION_ACTION_PATH})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ActionProcessor extends AbstractProcessor {

    private Logger logger;
    private Filer filer;
    private Map<String, ActionMeta> actionMap = new HashMap<>();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        logger = new Logger(processingEnv.getMessager());
        filer = processingEnv.getFiler();

    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        actionMap.clear();
        logger.info("start --- processor");

        Set<? extends Element> actionElements = roundEnv.getElementsAnnotatedWith(Action.class);
        logger.info("actions size" + actionElements.size());

        try {
            logger.info(">>> Found actions, start ... <<<");
            this.parseActions(actionElements);
            try {
                JavaFile javaFile = generateJavaSuperRFile();
                if (javaFile != null) {
                    javaFile.writeTo(filer);
                }
            } catch (IOException e) {
                logger.error("io exception :" + e);
            }
            logger.info(">>> generate actions, end !!! <<<");
        } catch (Exception e) {
            logger.error(e);
        }

        return true;
    }

    private void parseActions(Set<? extends Element> actionElements) throws IOException {

        for (Element e : actionElements) {
            if (e.getKind() == ElementKind.CLASS) {
                String sourcePath = ((TypeElement) e.getEnclosingElement()).getQualifiedName().toString();
                Action annotation = e.getAnnotation(Action.class);
                String name = annotation.name();
                actionMap.put(name, ActionMeta.build(name, sourcePath));
            }
        }

    }

    private JavaFile generateJavaSuperRFile() throws IOException {

        ArrayList<FieldSpec> fieldSpecs = new ArrayList<>();

        if (actionMap == null || actionMap.size() == 0) {
            return null;
        }

        for (Map.Entry<String, ActionMeta> entry : actionMap.entrySet()) {
            FieldSpec field = FieldSpec.builder(String.class, entry.getKey(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).initializer(entry.getValue().getSourcePath()).build();
            fieldSpecs.add(field);
        }

        logger.error("start generateJavaSuperFile ..... ");

        return JavaFile.builder(PACKAGE_OF_GENERATE_FILE,
                TypeSpec.classBuilder("SCMTable")
                        .addModifiers(Modifier.PUBLIC)
                        .addFields(fieldSpecs)
                        .build())
                .addFileComment(FILE_COMMENT)
                .build();

    }


}
