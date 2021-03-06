package cqa.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
/**
 * This file finds out the top 10 similar words to each word in question and comment from a pre-trained word2vec model
 * Not used in final feature set 
 * @author titas
 *
 */
public class word2vec {

    private static Logger log = LoggerFactory.getLogger(word2vec.class);

    public static void word2vecRun(String[] args) throws Exception {
    			
    			Word2Vec word2vec = WordVectorSerializer.loadFullModel(args[0]);          //load model
    			File file = new File(args[1]);
    			BufferedReader reader = null;
    			PrintWriter writer = null;
    			try {
    				writer = new PrintWriter(new BufferedWriter(new FileWriter(args[2], false)));
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			reader = new BufferedReader(new FileReader(file));
    			String line;
    				while((line = reader.readLine()) != null)
    				{
    					writer.println(line);
    					String[] qs = line.split("\\s+");
    					int num = Integer.parseInt(qs[1]);
    					String question = reader.readLine();
    					String[] splited = question.split("\\s+");
    					if(question.length() != 0)
    					{
	    					for(int j=0; j<splited.length; j++)
	    					{
	    						ArrayList<String> lst = new ArrayList<String>(word2vec.wordsNearest(splited[j], 10));      //find top 10 similar words
	    						if(splited[j].length() != 0)
	    						{
	    							writer.print(splited[j]+": ");
	    							for(int k=0; k<lst.size(); k++)
	    							{
	    								writer.print(lst.get(k)+" ");
	    							}
	    							writer.println();
	    						}
	    					}
    					}
    					for(int i=0; i<num; i++)
    					{
    						String str = reader.readLine();
							splited = str.split("\\s+");
							String c_id = splited[0];
							String label = splited[1];
							String comment = reader.readLine();
							writer.println(c_id);
							splited = comment.split("\\s+");
							if(comment.length() != 0)
							{
		    					for(int j=0; j<splited.length; j++)
		    					{
		    						ArrayList<String> lst = new ArrayList<String>(word2vec.wordsNearest(splited[j], 10));
		    						if(splited[j].length() != 0)
		    						{
		    							writer.print(splited[j]+": ");
		    							for(int k=0; k<lst.size(); k++)
		    							{
		    								writer.print(lst.get(k)+" ");
		    							}
		    							writer.println();
		    						}
		    					}
							}
    					}
    				}
    				writer.close();
    }
}
