public class LCSResult {
    int LCSLength;
    int s1LCSIndex;
    int s2LCSIndex;

    LCSResult(int length, int s1Index, int s2Index)
    {
         LCSLength  = length ;
         s1LCSIndex = s1Index;
         s2LCSIndex = s2Index;
    }
}
