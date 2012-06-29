package fixtures.common.transformers;

import java.util.Collection;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RecursiveToStringStyle extends ToStringStyle {
    private static final int INFINITE_DEPTH = -1;
    private static final long serialVersionUID = 1L;
    public static final ToStringStyle MULTI_LINE_STYLE = new RecursiveToStringStyle(5);

    public static final ToStringStyle MULTI_LINE_STYLE_FULLY_RECURSIVE = new RecursiveToStringStyle();

    /**
     * Setting {@link #maxDepth} to 0 will have the same effect as using original {@link #ToStringStyle}: it will
     * print all 1st level values without traversing into them. Setting to 1 will traverse up to 2nd level and so
     * on.
     */
    private int maxDepth;

    private int depth;

    public RecursiveToStringStyle() {
        this(INFINITE_DEPTH);
    }

    RecursiveToStringStyle(int maxDepth) {
        super();
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
        this.maxDepth = maxDepth;
        this.setContentStart("[");
        this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
        this.setFieldSeparatorAtStart(true);
        this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
        this.setUseIdentityHashCode(false);
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value.getClass().getName().startsWith("java.lang.") || (maxDepth != INFINITE_DEPTH && depth >= maxDepth)) {
            buffer.append(value);
        } else {
            depth++;
            buffer.append(ReflectionToStringBuilder.toString(value, this));
            depth--;
        }
    }

    // another helpful method
    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Collection coll) {
        depth++;
        buffer.append(ReflectionToStringBuilder.toString(coll.toArray(), this, true, true));
        depth--;
    }

    /**
     * <p>Ensure <code>Singleton</code> after serialization.</p>
     *
     * @return the singleton
     */
    private Object readResolve() {
        return ToStringStyle.MULTI_LINE_STYLE;
    }
}