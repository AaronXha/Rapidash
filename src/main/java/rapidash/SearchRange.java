package rapidash;

public class SearchRange {
    class Range{
        private int start;
        private int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public boolean contains(int value) {
            return value >= start && value <= end;
        }

        public int length() {
            return end - start + 1;
        }

        @Override
        public String toString() {
            return "[" + start + ", " + end + "]";
        }
    }

}
