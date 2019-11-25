import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.io.*;
import java.util.Arrays;
import java.util.function.BiFunction;


public class LCSPerformance {
    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    /* define constants */
    static int numberOfTrials = 50;
    static int MAXINPUTSIZE = (int) Math.pow(2,23);
    static int MININPUTSIZE = 1;

    //set up variable to hold folder path and FileWriter/PrintWriter for printing results to a file
    static String ResultsFolderPath = "/home/alyssa/Results/LCS/"; // pathname to results folder 
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;
    static String book, huck, tom, pride, sense;


    public static void main (String[] args) throws IOException
    {
        // read Adventures of Tom Sawyer into String tom replacing new line characters with spaces
        BufferedReader br = new BufferedReader(new FileReader("/home/alyssa/Desktop/adventuresOfTomSawyer.txt"));
        String nextLine = " ";
        StringBuffer sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null)
            sb.append(nextLine);
        tom = (sb.toString().replace('\n', ' '));

        // read Adventures of Huckleberry Finn into String huck replacing new line characters with spaces
         br = new BufferedReader(new FileReader("/home/alyssa/Desktop/adventuresOfHuckleberryFinn.txt"));
         sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null)
            sb.append(nextLine);
        huck = (sb.toString().replace('\n', ' '));

        //create a String book that has the contents of both books above
        book = tom+huck;

        // read Pride and Prejudice into String prid replacing new line characters with spaces
         br = new BufferedReader(new FileReader("/home/alyssa/Desktop/prideAndPrejudice.txt"));
         sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null)
            sb.append(nextLine);
        pride = (sb.toString().replace('\n', ' '));

        // read Sense and Sensibility into String sense replacing new line characters with spaces
        br = new BufferedReader(new FileReader("/home/alyssa/Desktop/senseAndSensibility.txt"));
        sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null)
            sb.append(nextLine);
        sense = (sb.toString().replace('\n', ' '));

        // create BiFunctions for each function do that they can be passed in a parameters and called from the same functions
        String [] LCSFuncNames = {"LCSBruteForce", "LCSIndexArray", "LCSGrid"};
        BiFunction<String, String, LCSResult> [] LCSFunc = (BiFunction<String, String, LCSResult>[])new BiFunction<?,?,?>[3];
        LCSFunc[0] = (String s1, String s2) -> { return LCS(s1, s2);};
        LCSFunc[1] = (String s1, String s2) -> { return LCSIndexArray(s1, s2);};
        LCSFunc[2] = (String s1, String s2) -> { return LCSGrid(s1, s2);};

        System.out.println("LCS Brute Force ");
        System.out.println("----------------");
        verifyLCS(LCSFunc[0]);
        System.out.println("LCS Index Array  ");
        System.out.println("----------------");
        verifyLCS(LCSFunc[1]);
        System.out.println("LCS Grid  ");
        System.out.println("----------------");
        verifyLCS(LCSFunc[2]);

      // LCS Brute Force
        numberOfTrials = 15;
        MAXINPUTSIZE = (int)Math.pow(2,16);
        for(int j = 2; j < 3; j ++) // performance test type
            for(int k = 0; k < 3; k++) // run number
                runFullExperiment(LCSFunc[0], LCSFuncNames[0] + "-Run"+(k+1) , j+1);

      // LCS Grid
        numberOfTrials = 15;
        MAXINPUTSIZE = (int)Math.pow(2,16);
        for(int j = 2; j < 3; j ++) // performance test type
            for(int k = 0; k < 3; k++) // run number
                runFullExperiment(LCSFunc[2], LCSFuncNames[2] + "-Run"+(k+1) , j+1);


        // LCS Index Array
        numberOfTrials = 35;
        MAXINPUTSIZE = (int)Math.pow(2,17);
        for(int j = 2; j < 3; j ++) // performance test type
            for(int k = 0; k < 3; k++) // run number
                runFullExperiment(LCSFunc[1], LCSFuncNames[1] + "-Run"+(k+1) , j+1);

        // LCS of Adventures of Huckleberry Finn and Adventures of Tom Sawyer both by Mark Twain
        LCSResult markTwain = LCS(huck, tom);
        System.out.println(huck.substring(markTwain.s1LCSIndex, markTwain.s1LCSIndex+markTwain.LCSLength));

        // LCS of Pride and Prejudice and Sense and Sensibility
        LCSResult janeAusten = LCS(pride, sense);
        System.out.println(pride.substring(janeAusten.s1LCSIndex, janeAusten.s1LCSIndex+janeAusten.LCSLength));
    }

    // verification that an LCS function is producing the correct results
    static void verifyLCS(BiFunction<String,String, LCSResult> func)
    {
        //random strings
        String s1 = randomString(500);
        String s2 = randomString(700);
        //get random substring from s2
        int sublen =(int) (Math.random()*700);
        int index1 =(int) (Math.random()*(700 - sublen));
        int index2 =(int) (Math.random()*500);
        String s1_1 = s1.substring(0,index2);
        String s1_2 = s2.substring(index1, index1+sublen);
        String s1_3 = s1.substring(index2);
        s1 = s1_1 + s1_2 + s1_3;
        displayLCS(s1,s2, func);

        // all matching characters
        displayLCS("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", func);

        // no matching characters
        displayLCS("ALLdddalkl;;;;sssKJHHSGD(%&SJDHAKA##%()", "iouqYTREEEmnvbbxcnXXXXmnbvvvvwWmzPPPP/'''====~", func);

        // pair more or less random, with a few short matching substrings ("MOO" "GO" and "sink")
        displayLCS("mimbfaiMOO.??/ed88wphelwoisink08fjn';;tyqGOajkdfnurewy90283", "woKH7875920934GObjkjk588HHMOO03ahj;l28KUGsinkvfiukljwerqfo", func);

        // pairs with a few different matching substrings of 5 - 10 ("SNOWFLAKE", "MOOSE", "IPHONE"
        displayLCS("MOOAIPHONEBPHCDMOOSEFSsgfuSNOWFPSNOWFLAKE", "BSNOWFLAKE897BFFIPHONET..8y.TXMOOSER", func);
        // ( "I went ", "day night ", " my sister K", " at ", ":00 at "
        displayLCS("I went to the store on Friday night with my sister Kelly at 9:00 at Hyvee", "I went swimming yesterday night at 7:00 at my sister Kallie's", func);

        // pairs with matching substring > half length of he longest of the two strings ("sookokokokNONO))))272726565mememememeLLLO")
        displayLCS("YYYYYEEEssssookokokokNONO))))272726565mememememeLLLOOOOl45645656", "AAAAsookokokokNONO))))272726565mememememeLLLOAAAAAAAAA", func);
        //("ARCO(((((((000190380989jsdifhewu")
        displayLCS("MARCO(((((((000190380989jsdifhewuh...,,,';=<><8679342", "psssssssssssssssstARCO(((((((000190380989jsdifhewu0333333333039393", func);
    }

    // find the LCS of two strings passed in, using the function passed in, and display the results
    static void displayLCS(String s1, String s2, BiFunction<String,String, LCSResult> func)
    {
        LCSResult result;
        int len, s1Index, s2Index;

        // run the LCS function with strings s1 and s2
        result = func.apply(s1, s2);
        // store the Results from LCSResult in local variables
        len = result.LCSLength;
        s1Index = result.s1LCSIndex;
        s2Index = result.s2LCSIndex;

        // print out the strings with brackets around the LCS and the LCS
        System.out.println("String 1 -->       " + s1.substring(0,s1Index) + "[" + s1.substring(s1Index, s1Index+len) + "]" + s1.substring(s1Index+len));
        System.out.println();
        System.out.println("String 2 -->       " + s2.substring(0,s2Index) + "[" + s2.substring(s2Index, s2Index+len) + "]" + s2.substring(s2Index+len));
        System.out.println();
        System.out.println("LCS      -->       " + s1.substring(s1Index, s1Index+len) );
        System.out.println();
    }

    //create a random list of integers of a specific length
    static String randomString(int size)
    {
        String random = "";
        // generate random number 32 - 126 as the ascii value of the character for each character of the string
        for(int i = 0; i < size; i++)
            random += (char)(Math.random() * (95)+32);
        return random;
    }

    // generate a string with the same random character repeating
    static String randomCharacterString(int size)
    {
        String random = "";
        char ch = (char)(Math.random() * (95)+32);
        //32 - 126
        for(int i = 0; i < size; i++)
            random += ch;
        return random;
    }

    //brute force algorithm for Longest Common Substring
    static LCSResult LCS(String s1, String s2)
    {
        // l1 is last index for string s1 and l2 is last index for string s2
        int s1LCSIndex = 0, s2LCSIndex = 0;
        int l1 = s1.length() - 1;
        int l2 = s2.length() - 1;
        int LCSLen = 0;

        // for each character in the first string, s1
        for(int i = 0;i <= l1; i++)
            // for each character in the second string s2
            for(int j = 0; j <= l2; j++)
                // check if they are equal and continue comparing the characters that follow until they don't match
                for(int k = 0; k <= Math.min(l1-i, l2-j); k++)
                {
                    // if current character in both strings don't match break
                    if (s1.charAt(i+k) != s2.charAt(j+k))
                        break;
                    // if the matching substring is longer than the current LCS, update it to be the LCS
                    if ( k+1 > LCSLen)
                    {
                        LCSLen = k+1;
                        s1LCSIndex = i;
                        s2LCSIndex = j;
                    }
                }
        // return the length and beginning index in both strings encapsulated in a LCSResult instance
        return new LCSResult(LCSLen, s1LCSIndex, s2LCSIndex);
    }

    static LCSResult LCSIndexArray(String s1, String s2)
    {
        int l1 = s1.length()-1;
        int l2 = s2.length()-1;
        // create an array of arrays that hold the indices that each character occurs at in s2
        // each character could occur up to l2 times (in a string of all one character)
        int chLoc [][] = new int [95][l2+2];
        int ch, k, j,  LCSLen = 0, s1Index = 0, s2Index = 0, index;
        // fill the index arrays with -1 which will be used to indicate there are no occurrences of a character
        // or that end of the index list for a character is reached
        for(int i = 0; i < 95; i++)
            Arrays.fill(chLoc[i], -1);
        // for each letter in s2
        for(int i = 0; i <= l2; i++)
        {
            // determine the location in the 2d array that represents that letter
            ch = s2.charAt(i)-32;
            k = 0;

            // get to the end of the indices already added
            while(chLoc[ch][k] != -1)
                k++;
            // add the current index to the characters indices array
            chLoc[ch][k] = i;
        }

        //for each letter in s1
        for ( int i = 0; i <= l1; i++)
        {
            // get the index of the chLoc array that holds the current character's indices in s2
            ch = s1.charAt(i)-32;
            index = 0;

            // get the index of the first occurence of the character in s2
            j = chLoc[ch][index];

            // if there isn't more characters left in s1 than there are in the current LCS, break
            if((l1 - i + 1) <= LCSLen)
                break;

            // while there are occurrences of the current character
            while ( j != -1 &&  ((l2 - j + 1) > LCSLen))
            {
                    for ( k = 0; k <= Math.min(l1 - i, l2 - j); k++)
                    {
                        // if current character in both strings don't match break or if characters current position + LCSLen aren't
                        // equal then the matching substring won't be longer than the current LCS so break
                        if (s1.charAt(i + k) != s2.charAt(j + k) || (( j+LCSLen <= l2&&i+LCSLen <= l1 ) && (s1.charAt(i + LCSLen) != s2.charAt(j + LCSLen))))
                            break;

                        // if the matching substring is longer than the current LCS, update it to be the LCS
                        if (k + 1 > LCSLen)
                        {
                            LCSLen = k + 1;
                            s1Index = i;
                            s2Index = j;
                        }
                    }
                // get the index of the next occurence of the current character in s2
                index++;
                j = chLoc[ch][index];
            }
        }
        return new LCSResult(LCSLen, s1Index, s2Index);
    }


    static LCSResult LCSGrid(String s1, String s2) {
        int l1 = s1.length() - 1;
        int l2 = s2.length() - 1;
        int LCSGrid[] = new int[Math.max(l1,l2) + 1];
        int x, y, iters;
        int LCSLength = 0, s1Index = 0, s2Index = 0;
        int max = Math.max(l1, l2);

        // for each character in the longer string of s1 and s2
        for(int i =max; i >=0; i--)
        {
            // if the remaining diagonals to evaluate represent shorter substrings than the current LCS, break
            if((i+1) <= LCSLength)
                break;

            // for each diagonal that is the same distance from the longest "center diagonal"
            for (int n = 1; n <=2; n++)
            {
                // x = max -1 for the first of the two diagonals and x = 0 for the second of the two diagonals
                x = (max - i) * (n%2);
                // y = 0 for the first of the two diagonals and y = max - i for the second of the two diagonals
                y = (max - i) * ((n+1)%2);
                // the iterations for the first of the two diagonals is l1 - (max -i)
                // and the iterations for the second of the two diagonals is l2 - (max - i)
                iters = (l1 - (max - i))*(n%2) + (l2 - (max-i))*((n+1)%2);

                // if the iterations surpass the length of the string (which will only the case with strings of different lengths)
                // assign the maximum possible iterations to iters
                if(x + iters > l1)
                    iters = l1 - x;
                if(y + iters > l2)
                    iters = l2 - y;

                // reset the grid to all 0s
                Arrays.fill(LCSGrid, 0);

                // for each iteration (each cell of the diagonal)
                for (int j = 0; j <= iters; j++)
                {
                    // if characters are the same in both strings
                    if (s1.charAt(x) == s2.charAt(y)) {
                        // if it is the first letter of the diagonal set it equal to 1
                        if (y == 0)
                            LCSGrid[y] = 1;
                        else
                        // otherwise add one to the value in the previous cell of the diagonal
                            LCSGrid[y] = LCSGrid[y - 1] + 1;

                        // if the length of the current substring is greater than the current LCS, set the current substring as the LCS
                        if (LCSGrid[y] > LCSLength) {
                            LCSLength = LCSGrid[y];
                            s1Index = x - LCSLength + 1;
                            s2Index = y - LCSLength + 1;
                        }
                    }
                    // move to the next cell in the diagonal
                    x++;
                    y++;
                }
                // if i == max, the center/longest diagonal is being evaluated and only needs to be evaluated once to set n to 3 so it would evaluate the diagonal again
                if( i == max)
                    n = 3;
            }
        }
        return new LCSResult(LCSLength, s1Index, s2Index);
    }


    // runs the sort function specified in function call for every input size for the specified number of trials
    // times the amount of time each trial took, and calculates the average for the input size
    // prints the input size along with the average time taken to run the sort function
    static void runFullExperiment(BiFunction<String, String, LCSResult> func, String resultsFileName, int option)
    {
        // add the test data type to the beginning of the file name
        if (option == 1)
            resultsFileName = "WorstCase-" + resultsFileName;
        else if (option == 2)
            resultsFileName = "RandomString-" + resultsFileName;
        else
            resultsFileName = "EnglishText-" + resultsFileName;
        String s1, s2;
        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);
        } catch(Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
            return;
        }

        // create stopwatch for timing individual trials
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch();

        // print the labels in the results file
        resultsWriter.println("#InputSize    AverageTime");
        resultsWriter.flush();

        //print a message to indicate the function and run that is currently running
        System.out.println(resultsFileName);

        //for each size of input we want to test: starting at MININPUTSIZE and doubling each iteration until reaching MAXINPUTSIZE
        for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize*= 2)
        {
            //print progress message
            System.out.println("Running test for input size "+inputSize+" ... ");
            System.out.print("    Running trial batch...");

            // reset elapsed time for the batch to 0
            long batchElapsedTime = 0;

            // force garbage collection before each batch of trials run
            System.gc();

            // repeat for desired number of trials (for a specific size of input)...
            for (long trial = 0; trial < numberOfTrials; trial++)
            {
                //worst case generate a string of input size characters with only one character
                if(option == 1)
                {
                     s1 = randomCharacterString(inputSize);
                     s2 = s1;
                }
                // random string testing generate two random strings of input size
                else if (option == 2)
                {
                     s1 = randomString(inputSize);
                     s2 = randomString(inputSize);
                }
                //grab random substrings of input size from book string
                else
                {
                    int index = (int) (Math.random() * (book.length() - inputSize));    s1 = book.substring(index, index + inputSize);
                    index = (int) (Math.random() * (book.length() - inputSize));
                    s2 = book.substring(index, index + inputSize);
                }

                // being timing
                TrialStopwatch.start();
                // run the LCS function on s1 and s2
                func.apply(s1,s2);
                //stop the timer and add to the total time elapsed for the batch of trials
                batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime();
            }
            // calculate the average time per trial in this batch
            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials;

            // print data for this size of input
            resultsWriter.printf("%12d  %15.2f \n",inputSize, averageTimePerTrialInBatch);
            resultsWriter.flush();
            System.out.println(" ....done.");
        }
    }
}
