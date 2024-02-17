import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Train
{
    private Vocabulary pos;
    private Vocabulary neg;
    private int totalPos;
    private int totalNeg;
    private int total;

    public Train() {}

	//dhmiourgei listes pos kai neg 
    public Train(ArrayList<String> voc) 
	{
        pos = new Vocabulary(voc);
        neg = new Vocabulary(voc);
    }

    public Vocabulary getPos() 
	{
        return pos;
    }

    public Vocabulary getNeg() 
	{
        return neg;
    }

    public int getTotalPos() 
	{
        return totalPos;
    }

    public int getTotalNeg() 
	{
        return totalNeg;
    }

    public int getTotal() 
	{
        return total;
    }

    public void readTrain(int j) 
	{
		//mpainei se auto to arxeio
        File myObj = new File(".\\aclImdb\\train\\labeledBow.feat");
        Scanner myReader = new Scanner(myObj);
        int counterDev = 0;
        int counterTrain = 0;
        while (myReader.hasNextLine()) 
		{
			String review = myReader.nextLine();
			//apothhkeuei se string array thn kathe grammh tou arxeiou
            String[] splitted = review.split(" ");
			//meta to split to prvto stoixeio tou string aaray einai h vathmologia
			//an h vathmologia einai thetikh
            if (Integer.parseInt(splitted[0]) >= 7) 
			{
                //metraei ta prwta 1250 pos gia dev
				if (counterDev < 1250) {
                    counterDev += 1;
                } 
				//ta j-1250 gia train
				else if(counterTrain < j - 1250)
				{
				//gia kathe leksh
					for (int i = 1; i < splitted.length; i++) 
					{
						//x:y me x na einai h leksh kai y einai oi fores pou emfanisthke
                        String[] splitToWord = splitted[i].split(":");
						//dhmiourgei to dianisma me 0-1
						pos.setDian(Integer.parseInt(splitToWord[0]));
						//perna sto pos to y ths x
                        pos.addCount(Integer.parseInt(splitToWord[0]), Integer.parseInt(splitToWord[1]));
                    }
                    counterTrain++;
                }
            } else {
				//metraei ta prwta 1250 neg gia dev
                if (counterDev < 2500) 
				{
                    counterDev += 1;
                } 
				//ta 2*j-2500 gia train
				else if(counterTrain < 2*j - 2500)
				{
					//gia kathe leksh
                    for (int i = 1; i < splitted.length; i++) 
					{
						//x:y me x na einai h leksh kai y einai oi fores pou emfanisthke
                        String[] splitToWord = splitted[i].split(":");
						//perna sto neg to y ths x
                        neg.addCount(Integer.parseInt(splitToWord[0]), Integer.parseInt(splitToWord[1]));
                    }
                    counterTrain+=1;
                }
            }
        }
        myReader.close();
	}

	//metraei synoliko arithmo thetikwn, arnhtikwn leksewn kai poses lekseis metrhse
    public void trainVocab() 
	{
        public totalPos=0;
        public totalNeg=0;
        total = 0;
        for(int i = 0; i < pos.getVocab().size();i++)
		{
            totalPos = pos.getWordCount().get(i)+totalPos;
            totalNeg = neg.getWordCount().get(i)+totalNeg;
			//auksanei to total gia na brei poses diaforetikes lekseis metrhse
            if(pos.getWordCount().get(i)!=0 || neg.getWordCount().get(i)!=0) 
			{
				total++;
			}
        }
    }

    public void removeHyperparametre(int n, int m, int k) 
	{
        //vriskei tis n pio suxnes lekseis
		for (int i=0; i<n; i++)
		{
			int max = -1;
			int max_position = -1;
			for (int i=0; i< pos.getVocab().size(); i++)
			{
				if (pos.getWordCount().get(i) >= max)
				{
					max = pos.getWordCount().get(i);
					max_position = i;
				}
				if (neg.getWordCount().get(i) >= max)
				{
					max = neg.getWordCount().get(i);
					max_position = i;
				}
			}
			//afairei thn pio suxnh leksh gia n epanalhpseis
            pos.getVocab().remove(max_position);
            pos.getWordCount().remove(max_position);
            neg.getVocab().remove(max_position);
            neg.getWordCount().remove(max_position);
        }
		
		//vriskei tis k pio spanies lekseis
		for (int i=0; i<k; i++)
		{
			int min = pos.getWordCount().get(0);
			int min_position = 0;
			for (int i=1; i< pos.getVocab().size(); i++)
			{
				if (pos.getWordCount().get(i) <= min)
				{
					min = pos.getWordCount().get(i);
					min_position = i;
				}
				if (neg.getWordCount().get(i) <= min)
				{
					min = neg.getWordCount().get(i);
					min_position = i;
				}
			}
			//afairei thn pio spania leksh gia k epanalhpseis
            pos.getVocab().remove(min_position);
            pos.getWordCount().remove(min_position);
            neg.getVocab().remove(min_position);
            neg.getWordCount().remove(min_position);
        }
        
		//dhmiourgia merged 
		ArrayList<String> merged = new ArrayList<String>();
		//gemisma ths merged me pos kai neg
		merged.addAll(pos);
		merged.addAll(neg);
		//sort ths merged me bubble sort
		for(int i=0; i < merged.getVocab().size(); i++)
		{  
            for(int j=1; j < merged.getVocab().size()-i); j++)
			{  
                if(merged.getWordCount().get(j-1) > merged.getWordCount().get(j))
				{    
                    temp = merged.getWordCount().get(j-1);  
                    merged.getWordCount().get(j-1) = merged.getWordCount().get(j);  
                    merged.getWordCount().get(j) = temp;
				}
			}
		}			
		//krata tis m lekseis
		for (int i=m; i < merged.getVocab().size(); i++)
		{
            merged.getVocab().remove(m);
            merged.getWordCount().remove(m);
		}
		//krata sto pos kai neg tis lekseis tou merged pou emeinan
		for (i=0; i < merged.getVocab().size(); i++)
		{
			//elegxei an h leksh vrisketai sthn pos
			boolean found = false;
			for (int j=0; j < pos.getVocab().size(); i++)
			{
				if (pos.getVocab().get(j) == merged.getVocab().get(i))
				{
					found = true;
				}
			}
			//an den vrei thn leksh thn afairei apo thn pos
			if (found == false)
			{
				pos.getVocab().remove(j);
				pos.getWordCount().remove(j);
			}
			else 
			{
				found = false;
			}
			//elegxei an h leksh vrisketai sthn neg
			for (int j=0; j < neg.getVocab().size(); i++)
			{
				if (neg.getVocab().get(j) == merged.getVocab().get(i))
				{
					found = true;
				}
			}
			//an den vrei thn leksh thn afairei apo thn neg
			if (found == false)
			{
				neg.getVocab().remove(j);
				neg.getWordCount().remove(j);
			}		
		}
    }
}