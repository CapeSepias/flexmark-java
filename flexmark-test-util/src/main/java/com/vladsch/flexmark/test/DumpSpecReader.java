package com.vladsch.flexmark.test;

import com.vladsch.flexmark.spec.SpecExample;
import com.vladsch.flexmark.spec.SpecReader;
import com.vladsch.flexmark.util.data.DataHolder;
import org.jetbrains.annotations.NotNull;
import org.junit.AssumptionViolatedException;

import java.io.InputStream;

import static com.vladsch.flexmark.test.TestUtils.FAIL;

public class DumpSpecReader extends SpecReader {
    protected final StringBuilder sb = new StringBuilder();
    protected final SpecExampleProcessor testCase;
    protected StringBuilder exampleComment;

    public DumpSpecReader(InputStream stream, SpecExampleProcessor testCase, String fileUrl) {
        super(stream, fileUrl);
        this.testCase = testCase;
    }

    public String getFullSpec() {
        return sb.toString();
    }

    @Override
    public void addSpecLine(String line) {
        sb.append(line).append("\n");
    }

    @Override
    protected void addSpecExample(@NotNull SpecExample example) {
        DataHolder exampleOptions;
        boolean ignoredTestCase = false;

        try {
            exampleOptions = TestUtils.getOptions(example, example.getOptionsSet(), testCase::options, testCase::combineOptions);
        } catch (AssumptionViolatedException ignored) {
            ignoredTestCase = true;
            exampleOptions = null;
        }

        if (exampleOptions != null && exampleOptions.get(FAIL)) {
            ignoredTestCase = true;
        }

        SpecExampleRenderer exampleRenderer = testCase.getSpecExampleRenderer(example, exampleOptions);

        SpecExampleParse exampleParse = new SpecExampleParse(exampleRenderer.getOptions(), exampleRenderer, exampleOptions, example.getSource());
        String source = exampleParse.getSource();
        boolean timed = exampleParse.isTimed();
        int iterations = exampleParse.getIterations();
        long start = exampleParse.getStartTime();
        long parse = exampleParse.getParseTime();

        String html;
        if (!ignoredTestCase) {
            html = exampleRenderer.renderHtml();
            for (int i = 1; i < iterations; i++) exampleRenderer.renderHtml();
        } else {
            html = example.getHtml();
        }
        long render = System.nanoTime();

        boolean embedTimed = TestUtils.EMBED_TIMED.getFrom(exampleRenderer.getOptions());

        String timingInfo = TestUtils.getFormattedTimingInfo(example.getSection(), example.getExampleNumber(), iterations, start, parse, render);

        if (timed || embedTimed) {
            System.out.println(timingInfo);
        }

        String ast = example.getAst() == null ? null : (!ignoredTestCase ? exampleRenderer.getAst() : example.getAst());

        // allow other formats to accumulate
        testCase.addSpecExample(exampleRenderer, exampleParse, exampleOptions, ignoredTestCase, html, ast);
        exampleRenderer.finalizeRender();

        if (embedTimed) {
            sb.append(timingInfo);
        }

        // include source so that diff can be used to update spec
        TestUtils.addSpecExample(sb, source, html, ast, example.getOptionsSet(), exampleRenderer.includeExampleInfo(), example.getSection(), example.getExampleNumber());
    }
}
