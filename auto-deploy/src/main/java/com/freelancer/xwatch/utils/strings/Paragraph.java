package com.freelancer.xwatch.utils.strings;

public final class Paragraph implements TextOption {
    private String start;
    private String end;
    private boolean oneLine;

    public Paragraph() {}

    public Paragraph(String start, String end) {
        this.start = start;
        this.end = end;
        this.oneLine = false;
    }

    public Paragraph(String start, String end, boolean oneLine) {
        this.start = start;
        this.end = end;
        this.oneLine = oneLine;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    public boolean isOneLine() {
        return this.oneLine;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setOneLine(boolean oneLine) {
        this.oneLine = oneLine;
    }

    public String process(String line) {
        return null;
    }

    @Override
    public String toString() {
        return "Paragraph [start=" + this.start + ", end=" + this.end + ", oneLine=" + this.oneLine + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.end == null ? 0 : this.end.hashCode());
        result = prime * result + (this.start == null ? 0 : this.start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Paragraph other = (Paragraph) obj;
        if (this.end == null) {
            if (other.end != null) {
                return false;
            }
        } else if (!this.end.equals(other.end)) {
            return false;
        }
        if (this.start == null) {
            if (other.start != null) {
                return false;
            }
        } else if (!this.start.equals(other.start)) {
            return false;
        }
        return true;
    }

}
