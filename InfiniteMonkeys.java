import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.security.SecureRandom;
import java.io.*;
import java.nio.file.*;
import java.util.*;


public class InfiniteMonkeys extends JFrame
{
   private static final String APPLICATION_VERSION          = "v0.005";// + "." + //", build: " +
                                                              //BuildNumberIncrementer.getBuildNumberFromInsideJAR ();
   private static final String APPLICATION_TITLE            = "Infinite Monkeys - " + APPLICATION_VERSION;

   private static final String CHARACTERS         = "     ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private static final int    SENTENCE_LENGTH    = 10;
   private static final int    SENTENCES_TO_FIND  =  5;
   private static final String WORDS_FILE         = "wordlist.dat";
   private static final int    ITERATIONS_PER_DOT = 10_000;

   JTextArea   outputTextArea                    = new JTextArea ();
   JScrollPane outputTextAreaScrollPane          = new JScrollPane (outputTextArea);
   JButton     generateRandomTextButton          = new JButton ("Generate");
   JButton     resetTextAreaButton               = new JButton ("Reset");

   ArrayList<String> wordsArrayList              = new ArrayList<String> ();


   public InfiniteMonkeys ()
   {
      setTitle (APPLICATION_TITLE);

      setLayout (new BorderLayout () );

      JPanel buttonPanel = new JPanel (new FlowLayout (FlowLayout.CENTER) );
      buttonPanel.add (generateRandomTextButton);
      buttonPanel.add (resetTextAreaButton);

      generateRandomTextButton.addActionListener (event -> generateRandomSentences () );
      resetTextAreaButton.addActionListener      (event -> resetTextArea () );

      add (outputTextAreaScrollPane, BorderLayout.CENTER);
      add (buttonPanel,              BorderLayout.SOUTH);

      try (BufferedReader inputFileReader = Files.newBufferedReader(Paths.get(WORDS_FILE) ) )
      {
         String lineStr = "";
         while ((lineStr = inputFileReader.readLine() ) != null)
         {
            wordsArrayList.add (lineStr);
         }
      }
      catch (IOException error)
      {
         error.printStackTrace();
      }


      System.out.println ("");
      System.out.println (APPLICATION_TITLE);
      System.out.println ("");
      System.out.println ("Java version: " + System.getProperty ("java.version") );
      System.out.println ("32/64 bit:    x" + System.getProperty ("sun.arch.data.model") );
      System.out.println ("java.vendor:  " + System.getProperty("java.vendor"));
      System.out.println ("java.home:    " + System.getProperty("java.home"));
      System.out.println ("OS.name:      " + System.getProperty("os.name"));
      System.out.println ("OS.arch:      " + System.getProperty("os.arch"));
      System.out.println ("OS.version:   " + System.getProperty("os.version"));

      System.out.println ("");
      System.out.println ("Words loaded: " + String.format ("%,d", wordsArrayList.size() ) +
                          " from '" + WORDS_FILE + "'." );
      System.out.println ("");

      System.out.println ("Generating random phrases " +
                          SENTENCE_LENGTH + " chars long ...");
      System.out.println ("");
   }

   private void generateRandomSentences ()
   {
      Date currentDateTime = new Date();
      System.out.println ("START: " + currentDateTime);
      System.out.println ("Every '.' is : " + String.format ("%,d", ITERATIONS_PER_DOT) + " iterations.");
      System.out.println ("Generating random phrases " + SENTENCE_LENGTH + " chars long ...");


      for (int k = 0; k < SENTENCES_TO_FIND; k++)
      {
         generateARandomSentence ();
      }

      currentDateTime = new Date();
      System.out.println ("");
      System.out.println ("END: " + currentDateTime);

      Toolkit.getDefaultToolkit().beep();
      Toolkit.getDefaultToolkit().beep();
      Toolkit.getDefaultToolkit().beep();
   }

   private void generateARandomSentence ()
   {
      /*
      Generate a random string of text.  How long ?
      20 chars
      Count how many times we have tried to generate a random sentence
      Count how many times we have succeeded.
      Calculate success percent.
      Check the string to see if it contains all English words - no gibberish.
      Stop if it contains all English words.

         "Cat hat jumped fish"                // OK
         "   Cat hat   jumped      fish    "  // OK
         "Cat hta jumped fish"                // Not OK

      isValidWord
      isValidEnglishWord

      What success rate do you expect ?
      1 in 100
      1 in 1,000
      1 in 10,000

      YES

      */

      boolean validSentence = false;

      SecureRandom  generator      = new SecureRandom();
      int iterationCount = 0;
      StringBuilder sentenceSb     = new StringBuilder ();
      String sentenceStr = "";

      int charactersArrayLength = CHARACTERS.length();


      while (validSentence == false)
      {

         sentenceSb     = new StringBuilder ();
         sentenceStr    = "";

         //char priorLetter = 'x';
         //int lengthSoFar  = 0;
         //for (int k = 0; k < SENTENCE_LENGTH; k++)
         while (sentenceStr.length() < SENTENCE_LENGTH)
         {
            // Append a random character onto our string
            // i.e. get a letter from CHARACTERS with index between
            // 0 and CHARACTERS.length - 1
            int  index  = generator.nextInt (charactersArrayLength );
            char letter = CHARACTERS.charAt (index);
            sentenceSb.append (letter);

            /*
            if (letter == ' ')
            {
               if (priorLetter != ' ')
                  sentenceSb.append (letter);
            }
            else
            */

            // For Testing:
            //sentenceSb = new StringBuilder ("   Cat    hat    jumped   fish     ");

            sentenceStr = sentenceSb.toString().trim().toLowerCase();

            while (sentenceStr.contains ("  ") == true)
            {
               sentenceStr = sentenceStr.replace ("  ", " ");
            }

         }

         iterationCount++;

         //outputTextArea.append (sentenceSb.toString() + "\n");

         // Check if sentence is all valid English words



         // For Testing:
         //sentenceSb = new StringBuilder ("   Cat    hat    jumped   fish     ");

         /*
         sentenceStr = sentenceSb.toString().trim().toLowerCase();

         while (sentenceStr.contains ("  ") == true)
         {
            sentenceStr = sentenceStr.replace ("  ", " ");
         }
         */

         String[] sentenceWordsArray = sentenceStr.split (" ");

         // For Testing:
         //for (int w = 0; w < sentenceWordsArray.length; w++)
         //{
         //   System.out.print ("'" + sentenceWordsArray [w] + "', ");
         //}
         //System.out.println ("");

         validSentence = true;

         for (int w = 0; w < sentenceWordsArray.length; w++)
         {
            if (wordsArrayList.contains (sentenceWordsArray[w]) == false)
            {
               //System.out.println ("-> NOT FOUND: '" + sentenceWordsArray[w] + "'");

               validSentence = false;
               w = sentenceWordsArray.length; // Exit loop
            }
         }

         //if (iterationCount > 1000)
         //   validSentence = true; // Exit Loop - just for testing.

         if (iterationCount % ITERATIONS_PER_DOT == 0)
         {
            System.out.print (".");
         }


         // For testing:
         //validSentence = true; // Exit Loop
      }

      if (iterationCount > ITERATIONS_PER_DOT)
      {
         System.out.println ();
      }

      System.out.print      ("'" + sentenceStr + "'" + " -- " + String.format ("%,d", iterationCount) + " tries !" + "\n");
      outputTextArea.append ("'" + sentenceStr + "'" + " -- " + String.format ("%,d", iterationCount) + " tries !" + "\n");
   }

   private void resetTextArea ()
   {
      outputTextArea.setText ("");
   }

   public static void main(String[] args)
   {
      InfiniteMonkeys app = new InfiniteMonkeys ();

      app.setSize (600, 600);
      app.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      app.setVisible (true);
   }


}