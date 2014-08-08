import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;


class ProbTableElement implements java.io.Serializable
{    
	private static final long serialVersionUID = 1L;
	double hamProb = 0.0;
    double spamProb = 0.0;
}

class WordElement
{    
	int hits = 0;
    int NoOfMails = 0;
    String LastOccuredInMail = null;
}

class DynamicState
{
	/*
	 * Training file data
	 */
	LinkedList<String> fileList = null;
	int hamN = 0;
	int spamN = 0;
	
	/*
	 * Elements for words in subject & message
	 */
	HashMap<String, Integer> vocabList = null;
	HashMap<String, WordElement> hamTextTable = null;
	HashMap<String, WordElement> spamTextTable = null;
	
	/*
	 * Elements for domains in received tag
	 */
	HashMap<String, Integer> domainList = null;
	HashMap<String, WordElement> hamDomainTable = null;
	HashMap<String, WordElement> spamDomainTable = null;
	
}


class StaticState implements java.io.Serializable
{    
	private static final long serialVersionUID = 1L;
	int totNoOfWordsInHam = 0;
	int totNoOfWordsInSpam = 0;
	int totNoOfDomainsInHam = 0;
	int totNoOfDomainsInSpam = 0;	
	double probHam = 0;
	double probSpam = 0; 
	HashMap<String, ProbTableElement> vocabWordProb = null;
	HashMap<String, ProbTableElement> domainProb = null;
	HashMap<String, Integer> stopWords = null;
}

class Mail 
{
	ArrayList<String> msg = null;
	ArrayList<String> from = null;
	// TODO: Add more items here
}

class IGElement 
{
	String key;
	Double value;
}

public class filter {
	
	private static boolean debug = false;
	
	private enum SECTIONS { SUBJECT, // Subject:
							MESSAGE_ID, // Message-Id:
							REFERENCES, // References:
							MIME_VERSION, // MIME-Version:
							CONTENT_TYPE, // Content-Type:
							CONTENT_DISPOSITION, // Content-Disposition:
							CONTENT_TRANSFER_ENCODING, // Content-Transfer-Encoding:
							USER_AGENT, // User-Agent:
							IN_REPLY_TO, // In-Reply-To:
							SENDER, // Sender:
							ERRORS_TO, // Errors-To:
							X_AND_SOMETHING, // X-...:
							PRECEDENCE, // Precedence:
							CC, // Cc:
							TO, // To:
							FROM, // From:
							DATE, // Date:
							RECEIVED, // Received:
							DELIVERED_TO, // Delivered-To:
							RETURN_PATH, // Return-Path:
							MAILING_LIST, // Mailing-List:
							LIST_AND_SOMETHING, // List-...:
							REPLY_TO, // Reply-To:
							MAIL_FOLLOWUP_TO, // Mail-Followup-To:
							DELIVERY_DATE, // Delivery-Date:
							MESSAGE,
							OTHER}

	private static double probInList(HashMap<String, ProbTableElement> textTable, String token, String str)
	{
		double prob = 0.0;
		
		ProbTableElement elem = textTable.get(token);
        if (elem != null)
    	{
    		if (str.equals("ham"))
    			prob = elem.hamProb;
    		else
    			prob = elem.spamProb;
    	}
        
        return prob;
	}
	
	private static double isThisASpamMail(String fileName, StaticState staticState) 
	{
		String token = null;
		double sumHam = 0.0;
		double sumSpam = 0.0;
		double temp;
		
		/*
		 * Parse mail and get the mail object
		 */
		Mail mail = parseMail(fileName, staticState);
		
		if (mail != null)
		{
			if (mail.msg != null)
			{
				Iterator<String> iterator = mail.msg.iterator();
				while(iterator.hasNext())
		    	{
		    		token = iterator.next().toString();
		    		
	            	temp = probInList(staticState.vocabWordProb, token, "ham");
	            	if (temp != 0.0)
	            		sumHam += Math.log(temp);
	        		
	            	temp = probInList(staticState.vocabWordProb, token, "spam");
	            	if (temp != 0.0)
	            		sumSpam += Math.log(temp);  
		    	}
			}
			
			//System.out.printf("\nsumHam %f sumSpam %f -- ", sumHam, sumSpam);
			if (mail.from != null)
			{
				Iterator<String> iterator = mail.from.iterator();
				while(iterator.hasNext())
		    	{
		    		token = iterator.next().toString();
		    		
		    		temp = probInList(staticState.domainProb, token, "ham");
	            	if (temp != 0.0)
	            		sumHam += Math.log(temp);
	        		
	            	temp = probInList(staticState.domainProb, token, "spam");
	            	if (temp != 0.0)
	            		sumSpam += Math.log(temp);  
		    	}
			}
		}
        
		//System.out.printf("sumHam %f sumSpam %f -- ", sumHam, sumSpam);
		
        sumHam += Math.log(staticState.probHam);
        
        sumSpam += Math.log(staticState.probSpam);   
        
		//System.out.printf("sumHam %f sumSpam %f", sumHam, sumSpam);
        //System.out.printf("(ham - spam %f) ", sumHam - sumSpam);
        
        return (sumHam - sumSpam);
        
        /*if (sumSpam > sumHam)
        	return true;
        else
        	return false;*/
	}
	
	private static int tokenOccInList(HashMap<String, WordElement> textTable, String token)
	{
		int times = 0;
		
        WordElement elem = textTable.get(token);

        if (elem != null) 
        {
            times = elem.hits;	
        }
        
        return times;
	}
	
	private static void updateTable(HashMap<String, Integer> textTable, String token)
	{
        Integer hits = textTable.get(token);
        if (hits != null) 
        {
            textTable.put(token, new Integer(hits.intValue() + 1));	
        }
        else 
        {
        	textTable.put(token, new Integer(1));
        }
	}
	
	private static void updateTable(HashMap<String, WordElement> textTable,
									String token, 
									String occuredInMail) 
	{
        WordElement elem = textTable.get(token);
        
        if (elem == null) 
        {
        	elem = new WordElement();
        	elem.hits = 1;
    		elem.NoOfMails = 1;
        	elem.LastOccuredInMail = occuredInMail;
        }
        else
        {
        	elem.hits += 1;
        	if (!elem.LastOccuredInMail.equals(occuredInMail))
        	{
        		elem.NoOfMails += 1;
            	elem.LastOccuredInMail = occuredInMail;
        	}
        }
    	
        textTable.put(token, elem);	
	}
	
	public static String textBwTags(StringBuilder str)
	{
		StringBuilder res = new StringBuilder();
		boolean tag = false;
		
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == '<')
			{
				tag = true;
				continue;
			}
			else if (str.charAt(i) == '>')
			{
				tag = false;
				res.append(" ");
				continue;
			}
			
			if (!tag)
			{
				res.append(str.charAt(i));
			}
		}
		
		return res.toString();
	}

	public static String parseReceivedStr(String received, String tag)
	{
		//System.out.println("received str :"+received);
		if (tag.equals("from"))
		{
			return received.replaceAll(".*from(.*)by.*", "$1").trim();
		}
		else if (tag.equals("by"))
		{
			return received.replaceAll(".*by[ ]*([a-z0-9A-Z.]*).*", "$1").trim();
		}
		else
			return null;
	}
	
	public static void addFrom(Mail mail, String from)
	{
		//System.out.println("From str :"+from);
		String[] fromText = from.split (" ");
		
    	if (fromText.length > 0) 
    	{	
    		for (int j = 0; j < fromText.length; j++)
    		{
    			if (j == 0 || j == fromText.length-1)
    			{
	    			fromText[j] = fromText[j].trim();
	    			
	    			if (fromText[j].contains("["))
	    				mail.from.add(fromText[j].substring(fromText[j].indexOf("[")+1, fromText[j].lastIndexOf("]")));
	    			else
	    			{
	    				//System.out.println("(3) fromText " + fromText[j]);
	    				mail.from.add(fromText[j]);
	    			}
    			}
    		}
    	}
	}
	
	
	public static Mail parseMail(String fileName, StaticState staticState)
	{
		Scanner scanStr = null;
		int lineCounter = 0;
        Mail mail = new Mail();
        
		if (mail.msg == null)
			mail.msg = new ArrayList<String>();
		
		if (mail.from == null)
			mail.from = new ArrayList<String>();
		
        try 
        {
            scanStr = new Scanner(new BufferedReader(new FileReader(fileName)));
            
            SECTIONS section = SECTIONS.OTHER;
            boolean emptyLineOccured = false;
    		boolean HTMLTAG_ON = false;
    		StringBuffer str = new StringBuffer();
    		StringBuilder htmlStr = new StringBuilder();
    		StringBuilder received = new StringBuilder();
        	String from;
        	String by;
        	
            while (scanStr.hasNext()) 
            {
            	/*
            	 * Under construction
            	 */
            	String line = scanStr.nextLine();
                int index = 0;

            	lineCounter++;
            	
            	if (line.isEmpty()) 
            	{
            		emptyLineOccured = true;
            		
            		if (received.length() > 0)
            		{
	            		// Record data
        				from = parseReceivedStr(received.toString(), "from");
            			by = parseReceivedStr(received.toString(), "by");
            			
            			if (from != null && by != null)
            			{
            				addFrom(mail, from);
            				addFrom(mail, by);
            			}
	            		received.setLength(0);
            		}
            		
            		// Do some resets
        			received.setLength(0);
        			
            		continue;
            	}
            	
            	line = line.trim();
            	/*
            	 * Recognise the message structure
            	 */
            	if (line.matches("<.*") && emptyLineOccured && !HTMLTAG_ON)
    			{
            		HTMLTAG_ON = true;
            		htmlStr.append(line);
            		//System.out.println("(1)");
            		continue;
    			}
            	else if (line.matches(".*>") && emptyLineOccured && HTMLTAG_ON)
            	{
            		HTMLTAG_ON = false;
            		htmlStr.append(line);
            		//System.out.println(htmlStr.toString());
            		line = textBwTags(htmlStr);
            		htmlStr.setLength(0);
            		//System.out.println("(2)");
            	}
            	else if (HTMLTAG_ON)
            	{
            		htmlStr.append(line);
            		//System.out.println("(3)");
            		continue;
            	}
            	
            	if (line.startsWith("Subject:"))
            	{
            		section = SECTIONS.SUBJECT;
            		// Exclude "Subject:"
            		index = 1;
            	}
            	else if (line.startsWith("Message-ID:") || line.startsWith("Message-Id:"))
            	{
            		section = SECTIONS.MESSAGE_ID;
            	}
            	else if (line.startsWith("Sender:")) 
            	{
            		section = SECTIONS.SENDER;
            	}
            	else if (line.startsWith("Date:"))
            	{
            		section = SECTIONS.DATE;
            	}
            	else if (line.matches("^X-(.)*:(.)*")) 
            	{
            		section = SECTIONS.X_AND_SOMETHING;
            	}
            	else if (line.matches("^List-(.)*:(.)*")) 
            	{
            		section = SECTIONS.LIST_AND_SOMETHING;
            	}
            	else if (line.startsWith("References:"))
            	{
            		section = SECTIONS.REFERENCES;
            	}
            	else if (line.startsWith("MIME-Version:") || line.startsWith("Mime-Version:"))
            	{
            		section = SECTIONS.MIME_VERSION;
            	}
            	else if (line.startsWith("Content-Type:"))
            	{
            		section = SECTIONS.CONTENT_TYPE;
            	}
            	else if (line.startsWith("Content-Disposition:"))
            	{
            		section = SECTIONS.CONTENT_DISPOSITION;
            	}
            	else if (line.startsWith("Content-Transfer-Encoding:"))
            	{
            		section = SECTIONS.CONTENT_TRANSFER_ENCODING;
            	}
            	else if (line.startsWith("User-Agent:"))
            	{
            		section = SECTIONS.USER_AGENT;
            	}
            	else if (line.startsWith("In-Reply-To:"))
            	{
            		section = SECTIONS.IN_REPLY_TO;
            	}
            	else if (line.startsWith("Errors-To:"))
            	{
            		section = SECTIONS.ERRORS_TO;
            	}
            	else if (line.startsWith("Precedence:"))
            	{
            		section = SECTIONS.PRECEDENCE;
            	}
            	else if (line.startsWith("Cc:"))
            	{
            		section = SECTIONS.CC;
            	}            	
            	else if (line.startsWith("To:"))
            	{
            		section = SECTIONS.TO;
            	}
            	else if (line.startsWith("From:"))
            	{
            		section = SECTIONS.FROM;
            	}
            	else if (line.startsWith("Mailing-List:"))
    			{
            		section = SECTIONS.MAILING_LIST;
    			}
            	else if (line.startsWith("Received:"))
            	{
            		section = SECTIONS.RECEIVED;
            	}
            	else if (line.startsWith("Delivered-To:"))
            	{
            		section = SECTIONS.DELIVERED_TO;
            	}
            	else if (line.startsWith("Reply-To:"))
            	{
            		section = SECTIONS.REPLY_TO;
            	}          
            	else if (line.startsWith("Return-Path:"))
            	{
            		section = SECTIONS.RETURN_PATH;
            	}
            	else if (line.startsWith("Delivery-Date:"))
            	{
            		section = SECTIONS.DELIVERY_DATE;
            	}
            	else if (line.startsWith("Mail-Followup-To:"))
            	{
            		section = SECTIONS.MAIL_FOLLOWUP_TO;
            	}
            	else if (line.startsWith("From") && (lineCounter == 1)) // only first line
            	{
                	section = SECTIONS.FROM;
            	}
            	else
            	{
            		if (line.matches(".*On.*wrote:") || line.matches(".*writes:") || line.matches(".*On.*mentioned:"))
	            	{
	            		section = SECTIONS.MESSAGE;
	            		continue; // Message must start in the next line
	            	}
            		else if (!emptyLineOccured)
            		{
            			//System.out.println("line:"+lineCounter+"for (1)");
            			section = SECTIONS.RECEIVED;
            		}
            		else
            		{
            			//System.out.println("line:"+lineCounter+"for (2)");
            			section = SECTIONS.MESSAGE;
            		}
            	}
            	
            	/*
            	 * Processing for Subject & Message
            	 */
            	if (section == SECTIONS.SUBJECT || section == SECTIONS.MESSAGE)
            	{
	            	String[] lineText = line.split (" ");
	            	
	            	if (lineText.length > 0) 
	            	{	
	            		for (int j = index; j < lineText.length; j++)
	            		{
	            			lineText[j] = lineText[j].trim();
	            			
	            			/*
	            			 * Ignore empty lines
	            			 */
	            			if (lineText[j].length() == 0)
	            				continue; // Ignore	
	            			
	            			/*
	            			 * Remove line breaks, designs and punctuation marks
	            			 */
	            			if (lineText[j].matches("[[\\p{Punct} && [^#$@]][\\p{Punct} && [^#$@]]*]+"))
	            				continue; // Ignore
	            			
	            			/*
	            			 * Remove direct urls
	            			 */
	            			if (lineText[j].startsWith("http:") || lineText[j].startsWith("<http:"))
	            				continue; // Ignore
	            			
	            			/*
	            			 * Remove phone numbers
	            			 */
	            			if (lineText[j].matches(".*[0-9]+-*[0-9]+.*"))
	            				continue; // Ignore
	            			
	            			/*
	            			 * Remove string of abnormal size
	            			 */
	            			if (lineText[j].length() > 30 || lineText[j].length() < 3)
	            				continue;
	            			
	            			/*
	            			 * Ignoring stop words
	            			 */
	            		    if (staticState.stopWords.containsKey(lineText[j]))
	            		    	continue;
	            			
	            			// Trim some punctuation marks around the words
	            			mail.msg.add(lineText[j].replaceAll("[\\[\'\"?:.,;)(]*([a-z]+ || [0-9]+)[?:.,;\'\"\\])]*", "$1").toLowerCase(Locale.ENGLISH));
	            		}
	            	}
            	}

            	/*
            	 * Processing for Received
            	 */
            	if (section == SECTIONS.RECEIVED)
            	{
            		
	            	if (line.startsWith("Received:"))
	            	{
	            		if (received.length() > 0)
	            		{
		            		// Record data
            				from = parseReceivedStr(received.toString(), "from");
	            			by = parseReceivedStr(received.toString(), "by");
	            			
	            			if (from != null && by != null)
	            			{
	            				addFrom(mail, from);
	            				addFrom(mail, by);
	            			}
		            		received.setLength(0);
	            		}
	            		
	            		// Add the current line to received
	            		received.append(line.replaceAll("Received:(.*)", "$1"));
	            	}
	            	else
	            	{
	            		// Add the current line to received
	            		//System.out.println("(1) append: "+line.toString());
	            		received.append(" ");
	            		received.append(line);
	            	}
	            	
            	}
            	
            	if (section == SECTIONS.FROM || section == SECTIONS.REPLY_TO || section == SECTIONS.RETURN_PATH)
            	{
            		str.setLength(0);
            		str.append(line.toString());
            		int i = str.indexOf("<");
            		if (i != -1)
            		{
	            		str.deleteCharAt(i);
	            		str.insert(i, " ");
            		}
            		
            		i = str.indexOf(">");
            		if (i != -1)
            		{
	            		str.deleteCharAt(i);
	            		str.insert(i, " ");
            		}
            		
            		String[] lineText = str.toString().split (" ");
            		
                	if (lineText.length > 0) 
                	{	
                		for (int j = 0; j < lineText.length; j++)
                		{
                			if (lineText[j].contains("@"))
                				mail.from.add(lineText[j]);
                		}
                	}
            	}
            }
            
        } 
        catch (IOException e) 
        {
            System.err.println("Caught IOException: " + e.getMessage());
            System.err.println("file name: " + fileName);            
            return null;
        }  
        finally 
        {
            if (scanStr != null) 
            {
                scanStr.close();
            }
        }	
        
        return mail;
	}
	
	static final Comparator<IGElement> VALUE_ORDER = 
            new Comparator<IGElement>() 
            {
				public int compare(IGElement e1, IGElement e2) 
				{
						return e2.value.compareTo(e1.value);
				}
            };

    public static void rmLowIGWords(HashMap<String, Integer> vocabList, 
    								HashMap<String, WordElement> hamTextTable, 
    								HashMap<String, WordElement> spamTextTable,
    								String tag,
    								StaticState state,
    								int totMails,
    								int hamN,
    								int spamN,
    								int size)
    {
		ArrayList<IGElement> vocabIGList = new ArrayList<IGElement>();
		
		Iterator<Map.Entry<String, Integer>> it = vocabList.entrySet().iterator();
		double ig = 0.0;
		double nrHamWithW;
		double nrSpamWithW;
		double fracMailsWithW;
		double fracMailsWithoutW;
		double classPurityWithW;
		double classPurityWithoutW;
		double fracMailsFromHamWithW;
		double fracMailsFromSpamWithW;
		double fracMailsFromHamWithoutW;
		double fracMailsFromSpamWithoutW;
		
		while (it.hasNext()) 
		{
			Map.Entry<String, Integer> entry = it.next();
			
			if (hamTextTable.containsKey(entry.getKey()))
				nrHamWithW = (double)hamTextTable.get(entry.getKey()).NoOfMails;
			else
				nrHamWithW = 0.0;
			
			if (spamTextTable.containsKey(entry.getKey()))
				nrSpamWithW = (double)spamTextTable.get(entry.getKey()).NoOfMails;
			else
				nrSpamWithW = 0.0;
			
			fracMailsWithW = (nrHamWithW + nrSpamWithW)/(totMails);
			fracMailsWithoutW = (totMails - (nrHamWithW + nrSpamWithW))/(totMails);
			
			//System.out.printf("fracMailsWithW, fracMailsWithoutW for word: %s : %f, %f\n", entry.getKey(), fracMailsWithW, fracMailsWithoutW);
			
			fracMailsFromHamWithW = (nrHamWithW/(nrHamWithW + nrSpamWithW));
			fracMailsFromSpamWithW = (nrSpamWithW/(nrHamWithW + nrSpamWithW));
			
			//System.out.printf("fracMailsFromHamWithW, fracMailsFromSpamWithW for word: %s : %f, %f\n", entry.getKey(), fracMailsFromHamWithW, fracMailsFromSpamWithW);
			
			fracMailsFromHamWithoutW = (((double)hamN - nrHamWithW) / (((double)hamN - nrHamWithW) + ((double)spamN - nrSpamWithW)));
			fracMailsFromSpamWithoutW = (((double)spamN - nrSpamWithW)/(((double)hamN - nrHamWithW) + ((double)spamN - nrSpamWithW)));
			
			//System.out.printf("fracMailsFromHamWithoutW, fracMailsFromSpamWithoutW for word: %s : %f, %f\n", entry.getKey(), fracMailsFromHamWithoutW, fracMailsFromSpamWithoutW);
			
			classPurityWithW = (fracMailsFromHamWithW) * (((fracMailsFromHamWithW != 0) ? Math.log(fracMailsFromHamWithW) : 0)/Math.log(2)) + 
						   (fracMailsFromSpamWithW) * (((fracMailsFromSpamWithW != 0) ? Math.log(fracMailsFromSpamWithW) : 0)/Math.log(2));
			
			classPurityWithoutW = (fracMailsFromHamWithoutW) * (((fracMailsFromHamWithoutW != 0) ? Math.log(fracMailsFromHamWithoutW) : 0)/Math.log(2)) + 
				   			   	  (fracMailsFromSpamWithoutW) * (((fracMailsFromSpamWithoutW != 0) ? Math.log(fracMailsFromSpamWithoutW) : 0)/Math.log(2));
			
			//System.out.printf("classPurityWithW, classPurityWithoutW for word: %s : %f, %f\n", entry.getKey(), classPurityWithW, classPurityWithoutW);
			
			ig = (fracMailsWithW) * (classPurityWithW) + (fracMailsWithoutW) * (classPurityWithoutW);
			
			//System.out.printf("ig for word: %s : %f\n", entry.getKey(), ig);
			IGElement elem = new IGElement();
			elem.key = entry.getKey();
			elem.value = Double.valueOf(ig);
			
			vocabIGList.add(elem);
		}
			
		/*
		* Sorted in ascending order!!
		*/
		Collections.sort(vocabIGList, VALUE_ORDER);
		
		if (vocabIGList.size() > size)
		{   
			for (int i = size; i < vocabIGList.size(); i++) 
			{ 
				vocabList.remove(vocabIGList.get(i).key);
				
				if (hamTextTable.containsKey(vocabIGList.get(i).key))
				{
					if (tag.equals("word"))
						state.totNoOfWordsInHam -= hamTextTable.get(vocabIGList.get(i).key).hits;
					else if (tag.equals("domain"))
						state.totNoOfDomainsInHam -= hamTextTable.get(vocabIGList.get(i).key).hits;
					hamTextTable.remove(vocabIGList.get(i).key);
				}
				if (spamTextTable.containsKey(vocabIGList.get(i).key))
				{
					if (tag.equals("word"))
						state.totNoOfWordsInSpam-= spamTextTable.get(vocabIGList.get(i).key).hits;
					else if (tag.equals("domain"))
						state.totNoOfDomainsInSpam-= spamTextTable.get(vocabIGList.get(i).key).hits;
					spamTextTable.remove(vocabIGList.get(i).key);
				}
			}
		}

    }
            
	public static void rmLowIGElems(DynamicState dynamicState,
									StaticState staticState)
	{
        /*
         * Applying feature: Remove words with low Information Gain
         * and limit the base size to half
         */
        rmLowIGWords(dynamicState.vocabList, 
        			 dynamicState.hamTextTable, 
        			 dynamicState.spamTextTable, 
        			 "word",
        			 staticState,
        			 dynamicState.fileList.size(), 
        			 dynamicState.hamN, 
        			 dynamicState.spamN, 
        			 (dynamicState.vocabList.size()/2));

        /*
         * Applying feature: Remove domains with low Information Gain
         * and limit the base size to half
         */
        rmLowIGWords(dynamicState.domainList, 
        			 dynamicState.hamDomainTable, 
        			 dynamicState.spamDomainTable, 
        			 "domain",
        			 staticState,
        			 dynamicState.fileList.size(), 
        			 dynamicState.hamN, 
        			 dynamicState.spamN, 
        			 (dynamicState.domainList.size()/2));
	}
	
	public static void rmInfrequentWords(DynamicState dynamicState,
										 StaticState staticState,
										 int times)
	{
		if (dynamicState.vocabList != null)
		{
		    Iterator<Map.Entry<String, Integer>> it = dynamicState.vocabList.entrySet().iterator();
		    while (it.hasNext()) 
		    {
		        Map.Entry<String, Integer> entry = it.next();
		        if (entry.getValue().intValue() < times)
				{
					// delete all entries for this key
		        	if (dynamicState.hamTextTable.containsKey(entry.getKey()))
		        	{
		        		staticState.totNoOfWordsInHam -= dynamicState.hamTextTable.get(entry.getKey()).hits;
		        		dynamicState.hamTextTable.remove(entry.getKey());
		        	}
		        	if (dynamicState.spamTextTable.containsKey(entry.getKey()))
		        	{
		        		staticState.totNoOfWordsInSpam-= dynamicState.spamTextTable.get(entry.getKey()).hits;
		        		dynamicState.spamTextTable.remove(entry.getKey());
		        	}
		        	it.remove(); // Avoid ConcurrentModificationException
				}
		    }
		}
		
		if (false)
		if (dynamicState.domainList != null)
		{
		    Iterator<Map.Entry<String, Integer>> it = dynamicState.domainList.entrySet().iterator();
		    while (it.hasNext()) 
		    {
		        Map.Entry<String, Integer> entry = it.next();
		        if (entry.getValue().intValue() < times)
		        {
					// delete all entries for this key
		        	if (dynamicState.hamDomainTable.containsKey(entry.getKey()))
		        	{
		        		staticState.totNoOfDomainsInHam -= dynamicState.hamDomainTable.get(entry.getKey()).hits;
		        		dynamicState.hamDomainTable.remove(entry.getKey());
		        	}
		        	if (dynamicState.spamDomainTable.containsKey(entry.getKey()))
		        	{
		        		staticState.totNoOfDomainsInSpam-= dynamicState.spamDomainTable.get(entry.getKey()).hits;
		        		dynamicState.spamDomainTable.remove(entry.getKey());
		        	}
		        	it.remove(); // Avoid ConcurrentModificationException
				}
		    }
		}
	}
			
	public static void rmSingleWordsFromDomain(DynamicState dynamicState, StaticState staticState)
	{
		Iterator<Map.Entry<String, Integer>> it = dynamicState.domainList.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry<String, Integer> entry = it.next();
	        if (!(entry.getKey().contains(".") || entry.getKey().contains("@")))
	        {
				// delete all entries for this key
	        	if (dynamicState.hamDomainTable.containsKey(entry.getKey()))
	        	{
	        		staticState.totNoOfDomainsInHam -= dynamicState.hamDomainTable.get(entry.getKey()).hits;
	        		dynamicState.hamDomainTable.remove(entry.getKey());
	        	}
	        	if (dynamicState.spamDomainTable.containsKey(entry.getKey()))
	        	{
	        		staticState.totNoOfDomainsInSpam-= dynamicState.spamDomainTable.get(entry.getKey()).hits;
	        		dynamicState.spamDomainTable.remove(entry.getKey());
	        	}
	        	it.remove(); // Avoid ConcurrentModificationException
			}
	    }
	}
	
	public static boolean addVocab(DynamicState dynamicState,
								   StaticState staticState,
			                       String fileName, 
			                       String className) 
	{
		String token = null;
		
		/*
		 * Parse mail and get the mail object
		 */
		Mail mail = parseMail(fileName, staticState);

		if (mail != null)
		{
			if (mail.msg != null)
			{
				Iterator<String> iterator = mail.msg.iterator();
				while(iterator.hasNext())
		    	{
					token = iterator.next().toString();
					
		    		updateTable(dynamicState.vocabList, token);
		    	
			    	if (className.equals("ham"))
			    	{
			    		updateTable(dynamicState.hamTextTable, token, fileName);
			            
			    		staticState.totNoOfWordsInHam++;
			    	}
			    	else 
			    	{
			    		updateTable(dynamicState.spamTextTable, token, fileName);
			            
			    		staticState.totNoOfWordsInSpam++;
			    	}
		    	}
			}
			
			if (mail.from != null)
			{
				Iterator<String> iterator = mail.from.iterator();
				while(iterator.hasNext())
		    	{
		    		token = iterator.next().toString();
		    		
		    		updateTable(dynamicState.domainList, token);
		    	
			    	if (className.equals("ham"))
			    	{
			    		updateTable(dynamicState.hamDomainTable, token, fileName);
			            
			    		staticState.totNoOfDomainsInHam++;
			    	}
			    	else 
			    	{
			    		updateTable(dynamicState.spamDomainTable, token, fileName);
			            
			    		staticState.totNoOfDomainsInSpam++;
			    	}
		    	}
			}
			else
				return false;
		}
		else
			return false;
		
        return true;
	}
	

	private static void trainAndTest(String trainDir)
	{
		/*
		 *  Read the training directory
		 */
		File directory = new File(trainDir);
		File[] listOfFiles = directory.listFiles();
		
		int noOfFiles = listOfFiles.length;
		int itemsInAFold = noOfFiles/10;
		
		File[][] folds = new File[10][itemsInAFold];
		int[] indexes = new int[10];
		
		/*
		 * Create 10 folds
		 */
		int fold;
		for (int i = 0; i < listOfFiles.length; i++)
		{
			fold = i % 10;
			folds[fold][indexes[fold]] = listOfFiles[i]; 
			indexes[fold]++;
			if (debug)
			System.out.printf("file : %s in fold : %d\n", folds[fold][--indexes[fold]].getName(), fold);
		}
		
		int wrongClassifications = 0;
		int totalClassifications = 0;
		
		for (int i = 0; i < 10; i++)
		{
			int testOn = i;
			File[] trainingFiles = new File[9*itemsInAFold];
			int index = 0;
			
			for (int j = 0; j < 10; j++)
			{
				if (j == testOn)
					continue;
				
				for (int k = 0; k < itemsInAFold; k++)
				{
					trainingFiles[index] = folds[j][k];
					index++;
				}
			}
			
			train(trainDir, trainingFiles);
			
			System.out.println(" \nTesting on fold : "+ testOn);
			for (int k = 0; k < itemsInAFold; k++)
			{
				double value = testMail(trainDir+"/"+folds[testOn][k].getName());
				String result;
				if (value < 0)
					result = "spam";
				else
					result = "ham";
				
				if (!folds[testOn][k].getName().contains(result))
				{
					System.out.printf("\n| Test mail : %s | Verdict : ", folds[testOn][k].getName());
					System.out.printf("%s | %s (ham-spam %f)|", result, folds[testOn][k].getName().contains(result) ? "correct" : "wrong", value);
				}
				
				if (!folds[testOn][k].getName().contains(result))
					wrongClassifications++;
				totalClassifications++;
			}
		}
		
		System.out.println("\n10 fold cross validation results:");
		System.out.printf("Percentage of correct classifications: %f \n", ((double)((totalClassifications-wrongClassifications)*100))/(double)totalClassifications);
		System.out.printf("Number of correct classifications: %d\n", (totalClassifications-wrongClassifications));
		System.out.printf("Number of wrong classifications: %d\n", (wrongClassifications));
	}
	
	private static void train(String trainDir, File[] listOfFiles)
	{

		String stopWordList[] = {"a","able","about","across","after","all","almost","also","am","among","an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","dear","did","do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how","however","i","if","in","into","is","it","its","just","least","let","like","likely","may","me","might","most","must","my","neither","no","nor","not","of","off","often","on","only","or","other","our","own","rather","said","say","says","she","should","since","so","some","than","that","the","their","them","then","there","these","they","this","tis","to","too","twas","us","wants","was","we","were","what","when","where","which","while","who","whom","why","will","with","would","yet","you","your"};

		/*
		 * Instantiate the dynamic state for training
		 */
		DynamicState dynamicState = new DynamicState();
		dynamicState.fileList = new LinkedList<String>();
		dynamicState.vocabList = new HashMap<String, Integer>(500);
		dynamicState.hamTextTable = new HashMap<String, WordElement>(500);
		dynamicState.spamTextTable = new HashMap<String, WordElement>(500);
		dynamicState.domainList = new HashMap<String, Integer>(500);
		dynamicState.hamDomainTable = new HashMap<String, WordElement>(500);
		dynamicState.spamDomainTable = new HashMap<String, WordElement>(500);

		/*
		 * Instantiate the static state for storing knowledge
		 */
		StaticState staticState = new StaticState();
		staticState.vocabWordProb = new HashMap<String, ProbTableElement>(500);
		staticState.domainProb = new HashMap<String, ProbTableElement>(500);
		staticState.stopWords = new HashMap<String, Integer>(stopWordList.length);
		
		for (int i = 0; i < stopWordList.length; i++)
		{
			// dummy value 1, no time to implement bloom filter
			staticState.stopWords.put(stopWordList[i], 1); 
		}
		
		for (int i = 0; i < listOfFiles.length; i++ )
		{
	        if (listOfFiles[i].getName().startsWith("ham") == true)
	        {
	        	dynamicState.hamN++;
	        }
	        else if (listOfFiles[i].getName().startsWith("spam") == true)
	        {
	        	dynamicState.spamN++;
	        }
	        dynamicState.fileList.add(listOfFiles[i].getName());		        	
		}
		
		staticState.probHam = (double)dynamicState.hamN / listOfFiles.length;
		staticState.probSpam = (double)dynamicState.spamN / listOfFiles.length;	
        
        // For 
        for ( String file : dynamicState.fileList )
        {
		    addVocab(dynamicState,
		    		 staticState,
		    		 trainDir + "/" + file, 
		    		 (file.startsWith("ham")? "ham": "spam"));	
        }
        
        /*
         * Remove single words from domain base
         */
        rmSingleWordsFromDomain(dynamicState, staticState);
        
        /*
         * Applying feature: Remove infrequent words which occur less
         * than 3 times
         */
        rmInfrequentWords(dynamicState, staticState, 3);
        
        /*System.out.println("After apply rmInfrequentWords:");
        System.out.printf("vocabList : %d\n", vocabList.size());
        System.out.printf("hamTextTable : %d\n", hamTextTable.size());
        System.out.printf("spamTextTable : %d\n", spamTextTable.size());
         */
        
        /*
         * Applying feature: Remove elements with low Information Gain
         * and limit the dictionary size to half
         */
        rmLowIGElems(dynamicState, 
        			 staticState);
        
        /*
         * Probabilities for word base
         */
        Map<String, Integer> map = dynamicState.vocabList;
        
        for (Map.Entry<String, Integer> entry : map.entrySet()) 
        {
            String token = entry.getKey();
        	
        	ProbTableElement newElem = new ProbTableElement();
        	
        	//ham probability
        	newElem.hamProb = ((double)tokenOccInList(dynamicState.hamTextTable, token) + 1.0)/
        						((double)staticState.totNoOfWordsInHam + (double)dynamicState.vocabList.size());
        	
        	//spam probability
        	newElem.spamProb = ((double)tokenOccInList(dynamicState.spamTextTable, token) + 1.0)/
        						((double)staticState.totNoOfWordsInSpam + (double)dynamicState.vocabList.size());
  
        	staticState.vocabWordProb.put(token, newElem);
        }
        
        // TODO: remove this
        /*System.out.println("SpamText output:");
        Iterator<Map.Entry<String, WordElement>> it = dynamicState.spamTextTable.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry<String, WordElement> entry = it.next();
	        System.out.printf("\n {%s} hits %d occuredinMails %d", entry.getKey(), entry.getValue().hits, entry.getValue().NoOfMails);        
		}*/
		
        
        /*
         * Probabilities for domain base
         */
        map = dynamicState.domainList;
        
        for (Map.Entry<String, Integer> entry : map.entrySet()) 
        {
            String token = entry.getKey();
        	
        	ProbTableElement newElem = new ProbTableElement();
        	
        	//ham probability
        	newElem.hamProb = ((double)tokenOccInList(dynamicState.hamDomainTable, token) + 1.0)/
        						((double)staticState.totNoOfDomainsInHam + (double)dynamicState.domainList.size());
        	
        	//spam probability
        	newElem.spamProb = ((double)tokenOccInList(dynamicState.spamDomainTable, token) + 1.0)/
        						((double)staticState.totNoOfDomainsInSpam + (double)dynamicState.domainList.size());
  
        	staticState.domainProb.put(token, newElem);
        }
        
        /*
         * Serialise StaticState
         */
        try 
        {
        	FileOutputStream fos = new FileOutputStream("StaticState"); 
        	ObjectOutputStream oos = new ObjectOutputStream(fos); 
			oos.writeObject(staticState);
	        oos.flush(); 
	        oos.close(); 
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
	}
	
	private static double testMail(String fileName)
	{
		StaticState staticState = new StaticState();

		// Create dummy state objects to suppress warning
		staticState.vocabWordProb = new HashMap<String, ProbTableElement>();
        
        /*
         * De-serialise staticState
         */
        try 
        {
        	FileInputStream fis = new FileInputStream("StaticState"); 
        	ObjectInputStream ois = new ObjectInputStream(fis); 
        	staticState = (StaticState)ois.readObject();
	        ois.close(); 
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		} 
        catch (ClassNotFoundException e) 
        {
			e.printStackTrace();
		} 
        
        //printHashTable(vocabWordProb);
        
        /*
         * Mail Classification
         */
        return isThisASpamMail(fileName , staticState);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args[0].equals("-t"))
		{
			System.out.println("Starting training...");
			/*
			 *  Read the training directory
			 */
			File directory = new File(args[1]);
			File[] listOfFiles = directory.listFiles();
			train(args[1], listOfFiles);
			System.out.println("Finished training...");
		}
		else if(args[0].equals("-tat"))
		{
			trainAndTest(args[1]);
		}
		else
		{
			double value = testMail(args[0]);
			String result;
			if (value < 0)
				result = "spam";
			else
				result = "ham";
			System.out.printf("%s\n", result);
		}
	}
}