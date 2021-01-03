import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.security.SecureRandom;
import java.io.*;
import java.nio.file.*;
import java.util.*;


public class InfiniteMonkeys extends JFrame
{

   private static final String CHARACTERS        = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private static final int    SENTENCE_LENGTH   = 20;

   JTextArea   outputTextArea                    = new JTextArea ();
   JScrollPane outputTextAreaScrollPane          = new JScrollPane (outputTextArea);
   JButton     generateRandomTextButton          = new JButton ("Generate");
   JButton     resetTextAreaButton               = new JButton ("Reset");

   ArrayList<String> wordsArrayList              = new ArrayList<String> ();


   public InfiniteMonkeys ()
   {
      setLayout (new BorderLayout () );

      JPanel buttonPanel = new JPanel (new FlowLayout (FlowLayout.CENTER) );
      buttonPanel.add (generateRandomTextButton);
      buttonPanel.add (resetTextAreaButton);

      generateRandomTextButton.addActionListener (event -> generateRandomText () );
      resetTextAreaButton.addActionListener      (event -> resetTextArea () );

      add (outputTextAreaScrollPane, BorderLayout.CENTER);
      add (buttonPanel,              BorderLayout.SOUTH);

      try (BufferedReader inputFileReader = Files.newBufferedReader(Paths.get("wordlist.dat") ) )
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



   }

   private void generateRandomText ()
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
      int sentenceCount = 0;
      StringBuilder sentenceSb     = new StringBuilder ();
      String sentenceStr = "";

      Date currentDateTime = new Date();
      System.out.println ("START: " + currentDateTime);

      while (validSentence == false)
      {

         sentenceSb     = new StringBuilder ();

         for (int k = 0; k < SENTENCE_LENGTH; k++)
         {
            // Append a random character onto our string
            // i.e. get a letter from CHARACTERS with index between
            // 0 and CHARACTERS.length - 1
            int  index  = generator.nextInt (CHARACTERS.length() );
            char letter = CHARACTERS.charAt (index);
            sentenceSb.append (letter);
         }

         sentenceCount++;

         //outputTextArea.append (sentenceSb.toString() + "\n");

         // Check if sentence is all valid English words



         // For Testing:
         //sentenceSb = new StringBuilder ("   Cat    hat    jumped   fish     ");

         sentenceStr = sentenceSb.toString().trim().toLowerCase();

         while (sentenceStr.contains ("  ") == true)
         {
            sentenceStr = sentenceStr.replace ("  ", " ");
         }


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

         //if (sentenceCount > 1000)
         //   validSentence = true; // Exit Loop - just for testing.

         if (sentenceCount % 1000 == 0)
            System.out.print (".");


         // For testing:
         //validSentence = true; // Exit Loop
      }

      currentDateTime = new Date();
      System.out.println ("");
      System.out.println ("END: " + currentDateTime);
      System.out.println ("Generating a valid sentence took: " +
                          sentenceCount + " tries !");
      System.out.println (sentenceStr );
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