package com.vladsch.flexmark.ext.gitlab;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.spec.SpecExample;
import com.vladsch.flexmark.test.ComboSpecTestCase;
import com.vladsch.flexmark.test.FlexmarkSpecExampleRenderer;
import com.vladsch.flexmark.test.SpecExampleRenderer;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboGitLabSpecTest extends ComboSpecTestCase {
    private static final String SPEC_RESOURCE = "/ext_gitlab_ast_spec.md";
    private static final DataHolder OPTIONS = new MutableDataSet()
            .set(HtmlRenderer.INDENT_SIZE, 2)
            //.set(HtmlRenderer.PERCENT_ENCODE_URLS, true)
            .set(HtmlRenderer.PERCENT_ENCODE_URLS, true)
            .set(HtmlRenderer.RENDER_HEADER_ID, true)
            .set(Parser.EXTENSIONS, Arrays.asList(GitLabExtension.create()));

    private static final Map<String, DataHolder> optionsMap = new HashMap<>();
    static {
        optionsMap.put("src-pos", new MutableDataSet().set(HtmlRenderer.SOURCE_POSITION_ATTRIBUTE, "md-pos"));
        optionsMap.put("no-del", new MutableDataSet().set(GitLabExtension.DEL_PARSER, false));
        optionsMap.put("no-ins", new MutableDataSet().set(GitLabExtension.INS_PARSER, false));
        optionsMap.put("no-quotes", new MutableDataSet().set(GitLabExtension.BLOCK_QUOTE_PARSER, false));
        optionsMap.put("no-math", new MutableDataSet().set(GitLabExtension.RENDER_BLOCK_MATH, false));
        optionsMap.put("no-mermaid", new MutableDataSet().set(GitLabExtension.RENDER_BLOCK_MERMAID, false));
        optionsMap.put("no-video", new MutableDataSet().set(GitLabExtension.RENDER_VIDEO_IMAGES, false));
        optionsMap.put("no-video-link", new MutableDataSet().set(GitLabExtension.RENDER_VIDEO_LINK, false));
        optionsMap.put("no-nested-quotes", new MutableDataSet().set(GitLabExtension.NESTED_BLOCK_QUOTES, false));
        optionsMap.put("block-delimiters", new MutableDataSet().set(GitLabExtension.BLOCK_INFO_DELIMITERS, "-"));
        optionsMap.put("math-class", new MutableDataSet().set(GitLabExtension.BLOCK_MATH_CLASS, "math-class"));
        optionsMap.put("mermaid-class", new MutableDataSet().set(GitLabExtension.BLOCK_MERMAID_CLASS, "mermaid-class"));
        optionsMap.put("code-content-block", new MutableDataSet().set(Parser.FENCED_CODE_CONTENT_BLOCK, true));
        optionsMap.put("video-extensions", new MutableDataSet().set(GitLabExtension.VIDEO_IMAGE_EXTENSIONS, "tst"));
        optionsMap.put("video-link-format", new MutableDataSet().set(GitLabExtension.VIDEO_IMAGE_LINK_TEXT_FORMAT, "Get Video '%s'"));
        optionsMap.put("video-class", new MutableDataSet().set(GitLabExtension.VIDEO_IMAGE_CLASS, "video-class"));
    }

    private static final Parser PARSER = Parser.builder(OPTIONS).build();
    // The spec says URL-escaping is optional, but the examples assume that it's enabled.
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

    private static DataHolder optionsSet(String optionSet) {
        if (optionSet == null) return null;
        return optionsMap.get(optionSet);
    }

    public ComboGitLabSpecTest(SpecExample example) {
        super(example);
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> data() {
        return getTestData(SPEC_RESOURCE);
    }

    @Nullable
    @Override
    public DataHolder options(String optionSet) {
        return optionsSet(optionSet);
    }

    @NotNull
    @Override
    public String getSpecResourceName() {
        return SPEC_RESOURCE;
    }
    @Override
    public @NotNull SpecExampleRenderer getSpecExampleRenderer(@NotNull SpecExample example, @Nullable DataHolder exampleOptions) {
        DataHolder combinedOptions = combineOptions(OPTIONS, exampleOptions);
        return new FlexmarkSpecExampleRenderer(example, combinedOptions, PARSER.withOptions(combinedOptions), RENDERER.withOptions(combinedOptions), true);
    }
}
