package utils;

import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class MethodInfo {
    private PsiElement psiElement;
    private String timeComplexityLong;
    private String timeComplexityShort;
    private String spaceComplexityLong;
    private String spaceComplexityShort;

    private PsiElement method;

    public MethodInfo(PsiElement psiElement) {
        this.psiElement = psiElement;
        this.timeComplexityLong = "";
        this.timeComplexityShort = "";
        this.spaceComplexityLong = "";
        this.spaceComplexityShort = "";
    }

    public String getTimeComplexityLong() {
        return timeComplexityLong;
    }

    public void setTimeComplexityLong(String timeComplexityLong) {
        this.timeComplexityLong = timeComplexityLong;
    }

    public String getTimeComplexityShort() {
        return timeComplexityShort;
    }

    public void setTimeComplexityShort(String timeComplexityShort) {
        this.timeComplexityShort = timeComplexityShort;
    }

    public String getSpaceComplexityLong() {
        return spaceComplexityLong;
    }

    public void setSpaceComplexityLong(String spaceComplexityLong) {
        this.spaceComplexityLong = spaceComplexityLong;
    }

    public String getSpaceComplexityShort() {
        return spaceComplexityShort;
    }

    public void setSpaceComplexityShort(String spaceComplexityShort) {
        this.spaceComplexityShort = spaceComplexityShort;
    }

    public PsiElement getMethod() {
        return method;
    }

    public void setMethod(PsiElement method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
