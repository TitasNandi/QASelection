package cqa.Feature_files.Java_files;



import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import info.debatty.java.stringsimilarity.CharacterSubstitutionInterface;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.KShingling;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.QGram;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.WeightedLevenshtein;

public class Similarity_feature_generator         //File generating various string features
{
	static double f_1, f_2, f_3, f_4, f_5, f_6, f_7, f_8, f_9, f_10, f_11, f_12, f_13, f_14, f_15;
	public static void main(String[] args)
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/RankLib/RankLib_file.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int q_id_rank = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String q_id = reader.readLine();
				do
				{
					String question = reader.readLine();
					q_id_rank++;
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						 f_1 = ngram(question, comment, 2);
						 f_2 = ngram(question, comment, 3);
						 f_3 = cosine(question, comment, 2);
						 f_4 = cosine(question, comment, 3);
						 f_5 = Jaccard(question, comment, 2);
						 f_6 = Jaccard(question, comment, 3);
						 f_7 = QGram(question, comment, 2);
						 f_8 = QGram(question, comment, 3);
						 f_9 = Sorensen(question, comment, 2);
						 f_10 = Sorensen(question, comment, 3);
						 f_11 = JaroWinkler(question, comment);
						 f_12 = Damerau(question, comment);
						 f_13 = Levenshtein(question, comment);
						 f_14 = NormalizedLevenshtein(question, comment);
						 f_15 = LCS(question, comment);
						 RankLib_writer(writer, label, q_id_rank, c_id);
						 //SVM_writer(writer, label, 1);
					}					
				}
				while((q_id = reader.readLine())!=null);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static void RankLib_writer(PrintWriter writer, String label, int q_id_rank, String c_id)
	{
		writer.println(get_Label_value(label)+" "+"qid:"+q_id_rank+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" 6:"+f_6+" 7:"+f_7+" 8:"+f_8+" 9:"+f_9+" 10:"+f_10+" 11:"+f_11+" 12:"+f_12+" 13:"+f_13+" 14:"+f_14+" 15:"+f_15+" # "+c_id);
	}
	public static void SVM_writer(PrintWriter writer, String label, int flag)
	{
		if(flag == 0)
			writer.println(get_Label_value(label)+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" 6:"+f_6+" 7:"+f_7+" 8:"+f_8+" 9:"+f_9+" 10:"+f_10);
		else
			writer.println(binary_class(label)+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" 6:"+f_6+" 7:"+f_7+" 8:"+f_8+" 9:"+f_9+" 10:"+f_10+" 11:"+f_11+" 12:"+f_12+" 13:"+f_13+" 14:"+f_14+" 15:"+f_15);
	}
	public static int get_Label_value(String s)
	{
		if(s.equals("Good"))
		{
			return 1;
		}
		else if(s.equals("PotentiallyUseful"))
		{
			return 2;
		}
		return 3;
	}
	public static int binary_class(String s)
	{
		if(s.equals("Good"))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public static double ngram(String s1, String s2, int n)
	{
		NGram ngram = new NGram(n);
		return ngram.distance(s1, s2);
	}
	public static double cosine(String s1, String s2, int n)
	{
		Cosine cos = new Cosine(n);
		if(Double.isNaN(cos.similarity(s1,s2)))
		{
			return 0.0;
		}
		return cos.similarity(s1, s2);
	}
	public static double Jaccard(String s1, String s2, int n)
	{
		Jaccard j2 = new Jaccard(n);
		return j2.similarity(s1, s2);
	}
	public static double QGram(String s1, String s2, int n)
	{
		QGram dig = new QGram(n);
		return dig.distance(s1, s2);
	}
	public static double Sorensen(String s1, String s2, int n)
	{
		SorensenDice sd = new SorensenDice(n);
		return sd.similarity(s1, s2);
	}
	public static double JaroWinkler(String s1, String s2)
	{
		JaroWinkler jw = new JaroWinkler();
		return jw.similarity(s1, s2);
	}
	public static double Damerau(String s1, String s2)
	{
		Damerau damerau = new Damerau();
		return damerau.distance(s1, s2);
	}
	public static double Levenshtein(String s1, String s2)
	{
		Levenshtein levenshtein = new Levenshtein();
		return levenshtein.distance(s1, s2);
	}
	public static double NormalizedLevenshtein(String s1, String s2)
	{
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		return l.distance(s1, s2);
	}
	public static double LCS(String s1, String s2)
	{
		LongestCommonSubsequence lcs = new LongestCommonSubsequence();
		return lcs.distance(s1, s2);
	}
}