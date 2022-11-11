public class Name {
    private String first, middle, last;

    public Name() {
    }

    public Name(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public Name(String first, String middle, String last) {
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public boolean hasMiddle() {
        return middle != null;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getName() {
        if (middle != null) {
            return first + " " + middle + " " + last;
        }
        return first + " " + last;
    }

    public boolean equals(Name name) {
        if (this.getFirst() == name.getFirst()) {
            if (this.getMiddle() == name.getMiddle()) {
                if (this.getLast() == name.getLast()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Name[first=" + first + ", middle=" + middle + ", last="  + last + "]";
    }

}
