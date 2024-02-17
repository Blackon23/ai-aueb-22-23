import java.util.ArrayList;

public class Vocabulary
{
    private ArrayList<String> vocab;
    private ArrayList<Integer> wordCount;

    public Vocabulary(ArrayList<String> voc) 
	{
		//ftiaxnei katallhles listes
        vocab = new ArrayList<>(voc.size());
		dian = new ArrayList<>(voc.size());
        wordCount = new ArrayList<>(voc.size());
        for(int i=0; i < voc.size(); i++)
		{
            vocab.add(voc.get(i));
			dian.add(0);
            wordCount.add(0);
        }
    }
	
	//posthetei ton ariumo emfanishs mias lekshs
    public void addCount(int index, int i) 
	{
        wordCount.set(index , wordCount.get(index) + i);
    }

	//epistrefei dianisma me 1-0 gia thn uparksh h oxi mias lekshs
	public ArrayList<String> getDian() 
	{
        return dian;
    }
	
	//vazei 1 an vrei thn leksh
	public void setDian(int i) 
	{
		dian.set(i, 1);
	}
	
	//epistrefei list vocab me lekseis ths review
    public ArrayList<String> getVocab() 
	{
        return vocab;
    }

	//epistrefei tis forsw pou emfanisthke mia leksh
    public ArrayList<Integer> getWordCount() 
	{
        return wordCount;
    }
}
