import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class N_B
{
    static ArrayList<String> voc = new ArrayList();
    int n=null;
	int m=null;
	int k=null;
	//gia accuracy
	ArrayList<float>[] A = new ArrayList[12];
	//for the test with best dev
	float[] B = new float[4]
	//gia precision - recall diagram
	ArrayList<float>[] P-R = new ArrayList[10];
	//gia f1 - threshold diagram
	ArrayList<float>[] F1 = new ArrayList[10];

    public static void main(String[] args) 
	{
        //oi lekseis twn aksiologisewn apo mia fora h kathe mia
		readVocabulary();
		
        Train train;
        float bestAcc = 0.0f;
        int bestSize=0;

		//vriskei tis uperparametrous
		findHyperparametre();
		//ksekina ton xrono
		long startTime = System.nanoTime();
		//dhmiourgoume tis listes positive kai negative me ta 1250 pos kai neg reviews gia dev
        Dev_Test dev = new Dev_Test();
		dev.Dev();
		
        for(int i=1500; i<=12500; i=i+1000)
		{
			//ftiaxnei listes
            train = new Train(voc);
			//diavazei ta train 
            train.readTrain(i);
			//afairei ta katallhla stoixeia
            train.removeHyperparametre(n,m,k);
			//metrhsh leksewn
            train.trainVocab();
			//dhmiourgoume tis listes positive kai negative me ta i-1250 pos kai neg reviews gia train
            Dev_Test trainData = new Dev_Test();
			trainData.Train(i);
			
            float trainAcc = trainData.calculate(train, accur, 0);
            float devAcc = dev.calculate(train, accur, 0);
			//apothhkeush apotelesmatwn accurancy gia dev kai train se pinaka A
			A[i/1000 - 1] = new ArrayList<float>(); 
			A[i/1000 - 1].add(2*i);
			A[i/1000 - 1].add(trainAcc);
			A[i/1000 - 1].add(devAcc);

            if(devAcc > bestAcc)
			{
                bestAcc = devAcc;
                bestSize = i;
            }
        }

        train = new Train(voc);
        train.readTrain(bestSize);
        train.removeHyperparametre(n,m,k);
        train.trainVocab();
        Dev_Test test = new Dev_Test();
		test.Test();
		
		//for the test with best dev i put them in a array [accuracy, precision, recall, f1Score]
		B[0] = test.calculate(train, accur, 0);
		B[1] = test.calculate(train, pres, 0);
		B[2] = test.calculate(train, rec, 0);
		B[3] = test.calculate(train, fl_Sc, 0);
		
		//for the first position of P-R array with thershold 0
		P-R[0] = new ArrayList<float>(); 
		P-R[0].add(test.calculate(train, pres, 0));
		P-R[0].add(test.calculate(train, rec, 0));
		i = 1;
		//to threshold den einai apo 0<threshold<1 giati exw log
        for(float t=0.1; t<=1; t=t+0.1) 
		{	
			//put the results in P-R array for every threshold
			P-R[i] = new ArrayList<float>(); 
			P-R[i].add(test.calculate(train, pres, t));
			P-R[i].add(test.calculate(train, rec, t));
			F1[i] = new ArrayList<float>(); 
			F1[i].add(test.calculate(train, f1, t));
			F1[i].add(t);
			i++;
		}
        long endTime   = System.nanoTime();
        long totalTime = (endTime - startTime)/1000000;
        System.out.println(totalTime);
    }
	
	//dokimastika kathe dunath epilogh hyperparametre dokimazetai gia na krathsoume autes pou dhmiourgoun to kalutero devAcc
	private static void findHyperparametre()
	{
		for (i=10; i<=100; i=i+10;)
		{
			for (j=2000; j<=5000; j=j+500;)
			{
				for (z=10; z<=100; z=z+10;)
				{
					Dev_Test dev = new Dev_Test();
					dev.Dev();
					train = new Train(voc);
					train.readTrain(12500);
					train.removeHyperparametre(i, j, z);
					train.trainVocab();
					Dev_Test trainData = new Dev_Test();
					trainData.Train(12500)
					float devAcc = dev.calculate(train);
					if(devAcc > bestAcc)
					{
						bestAcc = devAcc;
						n = i;
						m = j;
						k = z;
					}
				}
			}
		}
		float bestAcc = 0.0f;
	}
	
	//diavazei tis diathesimes lekseis (pou tha vriskontai sta reviews)
	private static void readVocabulary() 
	{
        File myObj = new File(".\\aclImdb\\imdb.vocab");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) 
		{
            voc.add(myReader.nextLine());
        }
        myReader.close();
    }
}
