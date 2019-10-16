package com.vladsch.flexmark.profiles.pegdown;

import com.vladsch.flexmark.spec.IRenderBase;
import com.vladsch.flexmark.util.ast.IRender;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;

import java.io.IOException;

class PegdownRenderer extends IRenderBase {
    final private ToHtmlSerializer mySerializer;
    final private int pegdownExtensions;

    public PegdownRenderer() {
        this(null);
    }

    public PegdownRenderer(DataHolder options) {
        super(options);
        pegdownExtensions = PegdownParser.getPegdownExtensions(options);
        mySerializer = new ToHtmlSerializer(new LinkRenderer());
    }

    @Override
    public void render(@NotNull Node node, @NotNull Appendable output) {
        assert node instanceof PegdownParser.PegdownRootNode;

        RootNode rootNode = ((PegdownParser.PegdownRootNode) node).myRootNode;
        String html = mySerializer.toHtml(rootNode);
        try {
            output.append(html);
            output.append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public IRender withOptions(@Nullable DataHolder options) {
        return new PegdownRenderer(options);
    }
}
