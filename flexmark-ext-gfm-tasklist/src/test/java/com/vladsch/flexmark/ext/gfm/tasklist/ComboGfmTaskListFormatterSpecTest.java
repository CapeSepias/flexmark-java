package com.vladsch.flexmark.ext.gfm.tasklist;

import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.spec.SpecExample;
import com.vladsch.flexmark.test.ComboSpecTestCase;
import com.vladsch.flexmark.test.FlexmarkSpecExampleRenderer;
import com.vladsch.flexmark.test.SpecExampleRenderer;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboGfmTaskListFormatterSpecTest extends ComboSpecTestCase {
    private static final String SPEC_RESOURCE = "/ext_gfm_tasklist_formatter_spec.md";
    private static final DataHolder OPTIONS = new MutableDataSet()
            //.set(FormattingRenderer.INDENT_SIZE, 2)
            //.set(HtmlRenderer.PERCENT_ENCODE_URLS, true)
            .set(Parser.EXTENSIONS, Collections.singleton(TaskListExtension.create()))
            .set(Parser.BLANK_LINES_IN_AST, true)
            .set(Parser.HEADING_NO_ATX_SPACE, true);

    private static final Map<String, DataHolder> optionsMap = new HashMap<>();
    static {
        //optionsMap.put("src-pos", new MutableDataSet().set(HtmlRenderer.SOURCE_POSITION_ATTRIBUTE, "md-pos"));
        //optionsMap.put("option1", new MutableDataSet().set(FormatterExtension.FORMATTER_OPTION1, true));
        optionsMap.put("no-suffix-content", new MutableDataSet().set(Parser.LISTS_ITEM_CONTENT_AFTER_SUFFIX, true));
        optionsMap.put("format-fixed-indent", new MutableDataSet().set(Formatter.FORMATTER_EMULATION_PROFILE, ParserEmulationProfile.FIXED_INDENT));
        optionsMap.put("task-case-as-is", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_CASE, TaskListItemCase.AS_IS));
        optionsMap.put("task-case-lowercase", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_CASE, TaskListItemCase.LOWERCASE));
        optionsMap.put("task-case-uppercase", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_CASE, TaskListItemCase.UPPERCASE));
        optionsMap.put("task-placement-as-is", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_PLACEMENT, TaskListItemPlacement.AS_IS));
        optionsMap.put("task-placement-incomplete-first", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_PLACEMENT, TaskListItemPlacement.INCOMPLETE_FIRST));
        optionsMap.put("task-placement-incomplete-nested-first", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_PLACEMENT, TaskListItemPlacement.INCOMPLETE_NESTED_FIRST));
        optionsMap.put("task-placement-complete-to-non-task", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_PLACEMENT, TaskListItemPlacement.COMPLETE_TO_NON_TASK));
        optionsMap.put("task-placement-complete-nested-to-non-task", new MutableDataSet().set(TaskListExtension.FORMAT_LIST_ITEM_PLACEMENT, TaskListItemPlacement.COMPLETE_NESTED_TO_NON_TASK));
        optionsMap.put("remove-empty-items", new MutableDataSet().set(Formatter.LIST_REMOVE_EMPTY_ITEMS, true));
    }

    private static final Parser PARSER = Parser.builder(OPTIONS).build();
    // The spec says URL-escaping is optional, but the examples assume that it's enabled.
    private static final Formatter RENDERER = Formatter.builder(OPTIONS).build();

    private static DataHolder optionsSet(String optionSet) {
        if (optionSet == null) return null;
        return optionsMap.get(optionSet);
    }

    public ComboGfmTaskListFormatterSpecTest(SpecExample example) {
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
