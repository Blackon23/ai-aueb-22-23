import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Dev_Test 
{
    private ArrayList<String> positive;
    private ArrayList<String> negative;

	public Dev_Test {}
	
	public ArrayList<String> getPositive() 
	{
        return positive;
    }

    public ArrayList<String> getNegative() 
	{
        return negative;
    }
	
	//gia dev
    public Dev() 
	{
		//listes pou apothhkeuetai to review me lekseis
        positive = new ArrayList<>();
		positive = readDevReviews("P");
        negative = new ArrayList<>();
        negative = readDevReviews("N");
    }
	
	//gia ta test
	public Test() 
	{
		positive = new ArrayList<>();
		positive = readTestReviews("P");
		negative = new ArrayList<>();
        negative = readTestReviews("N");
	}

	//gia ta train
    public Train(int i) 
	{
		//listes pou apouhkeuetai to review me lekseis
        positive = new ArrayList<>();
        negative = new ArrayList<>();
		positive = readTrainReviews("P", i);
        negative = readTrainReviews("N", i);
    }

    public float calculate(Train train, String t, float th)
	{
		//athroisma thetikwn pou bghkan ontws thetikoi
        int truePos = testData(positive, train, "P", th);
		//athroisma thetikwn pou bghkan arnhtikoi
        int falseNeg = positive.size() - truePos;
		//athroisma negative pou bghkan ontws negative
        int trueNeg = testData(negative, train, "N", th);
		//athroisma negative pou bghkan positive
        int falsePos = negative.size() - trueNeg;
		//analoga me to t epistrefei kai thn antistoixh timh
		if (t == accur)
		{
			return ((truePos + trueNeg)*100)/(truePos + trueNeg + falseNeg + falsePos);
		}
		else if (t == prec)
		{
			return (((truePos*100 / (truePos+falsePos)) + (trueNeg*100 / (trueNeg+falseNeg))) / 2);
		}
		else if (t == rec) 
		{
			return (((truePos*100 / (truePos+falseNeg)) + (trueNeg*100 / (trueNeg+falsePos))) / 2);
		}
		else if (t == f1) 
		{
			float precision = ((truePos*100 / (truePos+falsePos)) + (trueNeg*100 / (trueNeg+falseNeg))) / 2;
			float recall = ((truePos*100 / (truePos+falseNeg)) + (trueNeg*100 / (trueNeg+falsePos))) / 2;
			//F-measure for b=1
			return 2*precision*recall/(precision + recall);
		}
    }

	//epistrofh arithmou pos h neg train
    public int testData(ArrayList<String> review, Train train, String flag, float threshold) 
	{
        int number = 0;
        for (int i=0; i<review.size(); i++) 
		{
			//afairei ta shmeia stikshs kena klp
            String[] arr = review.get(i).split("\\W+");

            int index;
            double probP = 0;
            double probN = 0;

            for (int j = 0; j < arr.length; j++) 
			{
				//an periexetai h leksh arr[j] sthn list twn leksewn
				if (train.getPos().getVocab().contains(arr[j].toLowerCase()) || train.getNeg().getVocab().contains(arr[j].toLowerCase()))
				{
					if (flag == "P")
					{
						//index = h thesh ths lekshs sthn list
						index = train.getPos().getVocab().indexOf(arr[j].toLowerCase());
						//an sto index exw 1 dhladh an uparxei h leksh sto vocab tou positive train
						if (train.getPos().getDian().get(index) == 1) 
						{ 
							//pithaniothta = pithanothta + log tou sunolikou athroismatos ths lekshs + 1 gia laPlace dia tou athroismatos twn thetikwn me to sunolo twn leksewn
							probP = probP + Math.log((double) (train.getPos().getWordCount().get(index)+1) / (train.getTotalPos() + train.getTotal()));
							probN = probN + Math.log((double) (train.getNeg().getWordCount().get(index)+1) / (train.getTotalNeg() + train.getTotal()));
						}
						else
						{
							probP = Math.log(1.0 / (train.getTotalPos() + train.getTotal())) + probP;
							probN = Math.log(1.0 / (train.getTotalNeg() + train.getTotal())) + probN;
						} 
					}
					else 
					{
						index = train.getNeg().getVocab().indexOf(arr[j].toLowerCase());
						//an sto index exw 1 dhladh an uparxei h leksh sto vocab tou negative train
						if (train.getNeg().getDian().get(index) == 1) 
						{ 
							//pithaniothta = pithanothta + log tou sunolikou athroismatos ths lekshs + 1 gia laPlace dia tou athroismatos twn thetikwn me to sunolo twn leksewn
							probP = probP + Math.log((double) (train.getPos().getWordCount().get(index)+1) / (train.getTotalPos() + train.getTotal()));
							probN = probN + Math.log((double) (train.getNeg().getWordCount().get(index)+1) / (train.getTotalNeg() + train.getTotal()));
						}
						else
						{
							probP = Math.log(1.0 / (train.getTotalPos() + train.getTotal())) + probP;
							probN = Math.log(1.0 / (train.getTotalNeg() + train.getTotal())) + probN;
						}
					}
				}
            }
			//sugkrish pithanothtwn
			//an exw threshold
            if (threshold == 0)
			{
				if(flag == "P") 
				{
					if (probP > probN)
					{
						number += 1;
					}
				}
				else
				{
					if (probN > probP) 
					{
						number += 1;
					}
				}
			}
			//an den exw threshold
			else
			{
				if(flag == "P") 
				{
					if (probP>= threshold) 
					{
						number += 1;
					}
				}
				else
				{
					if (probN< threshold) 
					{
						number += 1;
					}
				}
			}
        }
        return number;
    }
	
	public ArrayList<String> readDevReviews(String l) 
	{
		//kenh lista review pou meta tha epistrepsei
        ArrayList<String> review = new ArrayList<>();
        File myObj = null;
        File[] listofFiles;
        Scanner myReader;
		//analoga me to l epilegoume fakelo pou ua doume ta reviews
        if (l == "P")
		{
			myObj = new File(".\\aclImdb\\train\\pos");
		}
		else if (l == "N")
		{
			myObj = new File(".\\aclImdb\\train\\neg");
		}
			
		//vazei ston pinaka listofFiles ola ta arxeia me tis kritikes
        listofFiles = myObj.listFiles();  
        int i = 0;
        //gia kathe review
		for (File file : listofFiles) 
		{
            myReader = new Scanner(file);
            String rev = " ";
			i++;
			//oso uparxoun akoma lekseis sto review
            while (myReader.hasNext())
			{
                String add = myReader.nextLine();
                //ennonei to review sto string rev
				rev = rev.concat(" " + add);
            }
			//vazei to rev sto review
            if(!rev.equals(" ")) review.add(rev)
			{
				//an ftasei ta 1250 dev stamataei
				if(i >= 1250) 
				{
                    myReader.close();
                    break;
                }  
            }
        }
		myReader.close();
		return review;
	}
	
	public ArrayList<String> readTrainReviews(String l, int i) 
	{
		//kenh lista review pou meta tha epistrepsei
        ArrayList<String> review = new ArrayList<>();
        File myObj = null;
        File[] listofFiles;
        Scanner myReader;
		//analoga me to l epilegoume fakelo pou ua doume ta reviews
		if (l == "P")
		{
			myObj = new File(".\\aclImdb\\train\\pos");
		}
		else if (l == "N")
		{
			myObj = new File(".\\aclImdb\\train\\neg");
		}
			
		//vazei ston pinaka listofFiles ola ta arxeia me tis kritikes
        listofFiles = myObj.listFiles();  
        int j = 0;
        //gia kathe review
		for (File file : listofFiles) 
		{
            myReader = new Scanner(file);
			//prepei na diavasei apo to 1250 kai panw logw dev
            if (j >= 1250) 
			{
                String rev = " ";
                while (myReader.hasNextLine()) 
				{
                    String add = myReader.nextLine();
                     rev = rev.concat(" " + add);
                }
                if(!rev.equals(" ")) review.add(rev)
				{
				//an ftasei to orio stamataei
					if(j >= i) 
					{
						myReader.close();
						break;
					}
				}
				j++;
			}
        }
		myReader.close();
		return review;
	}
	
	public ArrayList<String> readTestReviews(String l) 
	{ 
		//kenh lista review pou meta tha epistrepsei
        ArrayList<String> review = new ArrayList<>();
        File myObj = null;
        File[] listofFiles;
        Scanner myReader;
		//analoga me to l epilegoume fakelo pou ua doume ta reviews
		if (l == "P")
		{
			myObj = new File(".\\aclImdb\\train\\pos");
		}
		else if (l == "N")
		{
			myObj = new File(".\\aclImdb\\train\\neg");
		}
			
		//vazei ston pinaka listofFiles ola ta arxeia me tis kritikes
        listofFiles = myObj.listFiles();  
        
		for (File file : listofFiles) 
		{
            myReader = new Scanner(file);
            String rev = " ";
            while (myReader.hasNextLine())
			{
                String add = myReader.nextLine();
                rev = rev.concat(" " + add);
            }
            if(!rev.equals(" ")) 
			{
				review.add(rev);
            }
			myReader.close();
		}
		return review;
	}
}
