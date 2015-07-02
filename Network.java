
public class Network {


	 /**
	  * The global error for the training.
	  */
	 protected double globalError;

	 /**
	  * The number of input neurons.
	  */
	 protected int inputCount;

	 /**
	  * The number of hidden neurons.
	  */
	 protected int hiddenCount;

	 /**
	  * The number of output neurons
	  */
	 protected int outputCount;

	 /**
	  * The total number of neurons in the network.
	  */
	 protected int neuronCount;

	 /**
	  * The number of weights in the network.
	  */
	 protected int weightCount;

	 /**
	  * The learning rate.
	  */
	 protected double learnRate;

	 /**
	  * The outputs from the various levels.
	  */
	 protected double fire[];

	 /**
	  * The weight matrix this, along with the thresholds can be
	  * thought of as the "memory" of the neural network.
	  */
	 protected double matrix[];

	 /**
	  * The errors from the last calculation.
	  */
	 protected double error[];

	 /**
	  * Accumulates matrix delta's for training.
	  */
	 protected double accMatrixDelta[];

	 /**
	  * The thresholds, this value, along with the weight matrix
	  * can be thought of as the memory of the neural network.
	  */
	 protected double thresholds[];

	 /**
	  * The changes that should be applied to the weight
	  * matrix.
	  */
	 protected double matrixDelta[];

	 /**
	  * The accumulation of the threshold deltas.
	  */
	 protected double accThresholdDelta[];

	 /**
	  * The threshold deltas.
	  */
	 protected double thresholdDelta[];

	 /**
	  * The momentum for training.
	  */
	 protected double momentum;

	 /**
	  * The changes in the errors.
	  */
	 protected double errorDelta[];


	 /**
	  * Construct the neural network.
	  *
	  * @param inputCount The number of input neurons.
	  * @param hiddenCount The number of hidden neurons
	  * @param outputCount The number of output neurons
	  * @param learnRate The learning rate to be used when training.
	  * @param momentum The momentum to be used when training.
	  */
	 public Network(String inputCount1,
			 String hiddenCount1,
			 String outputCount1,
			 String learnRate1,
			 String momentum1) {

	  this.learnRate = Double.parseDouble(learnRate1);
	  this.momentum = Double.parseDouble(momentum1);

	  this.inputCount = Integer.parseInt(inputCount1);
	  this.hiddenCount = Integer.parseInt(hiddenCount1);
	  this.outputCount = Integer.parseInt(outputCount1);
	  neuronCount = inputCount + hiddenCount + outputCount;
	  weightCount = (inputCount * hiddenCount) + (hiddenCount * outputCount);

	  fire    = new double[neuronCount];
	  matrix   = new double[weightCount];
	  matrixDelta = new double[weightCount];
	  thresholds = new double[neuronCount];
	  errorDelta = new double[neuronCount];
	  error    = new double[neuronCount];
	  accThresholdDelta = new double[neuronCount];
	  accMatrixDelta = new double[weightCount];
	  thresholdDelta = new double[neuronCount];

	  reset();           // * Method Calling
	 }



	 /**
	  * Returns the root mean square error for a complet training set.
	  *
	  * @param len The length of a complete training set.
	  * @return The current error for the neural network.
	  */
	 public double getError(int len) {
	  double err = Math.sqrt(globalError / (len * outputCount));
	  globalError = 0; // clear the accumulator
	  return err;

	 }

	 /**
	  * The threshold method. You may wish to override this class to provide other
	  * threshold methods.
	  *
	  * @param sum The activation from the neuron.
	  * @return The activation applied to the threshold method.
	  */
	 public double threshold(double sum) {
	  return 1.0 / (1 + Math.exp(-1.0 * sum));
	 }

	 /**
	  * Compute the output for a given input to the neural network.
	  *
	  * @param input The input provide to the neural network.
	  * @return The results from the output neurons.
	  */
	 public double []computeOutputs(double input[]) {
	  int i, j;
	  final int hiddenIndex = inputCount;
	  final int outIndex = inputCount + hiddenCount;

	  for (i = 0; i < inputCount; i++) {
	   fire[i] = input[i];
	  }

	  // first layer
	  int inx = 0;

	  for (i = hiddenIndex; i < outIndex; i++) {
	   double sum = thresholds[i];                  //Constant Threshold =0.5

	   for (j = 0; j < inputCount; j++) {
	    sum += fire[j] * matrix[inx++];
	   }
	   fire[i] = threshold(sum);
	  // System.out.println("The Output for the Hidden Layer Neuron "+(i+1)+" is: "+fire[i]);
	  }

	  // hidden layer

	  double result[] = new double[outputCount];

	  for (i = outIndex; i < neuronCount; i++) {
	   double sum = thresholds[i];

	   //System.out.println("Adding Thresholds and Calculating output!!");
	   for (j = hiddenIndex; j < outIndex; j++) {
	    sum += fire[j] * matrix[inx++];
	   }
	   fire[i] = threshold(sum);      //Calling for calculating Sigmond Function;
	  // System.out.println(i);
	   result[i-outIndex] = fire[i];
	   //System.out.println("The Actual Output is :"+result[i-outIndex]);
	  }

	  return result;
	 }


	 /**
	  * Calculate the error for the recogntion just done.
	  *
	  * @param ideal What the output neurons should have yielded.
	  */
	 public void calcError(double ideal[]) {
	  int i, j;
	  final int hiddenIndex = inputCount;
	  final int outputIndex = inputCount + hiddenCount;

	  // clear hidden layer errors
	  for (i = inputCount; i < neuronCount; i++) {
	   error[i] = 0;
	  }

	  // layer errors and deltas for output layer
	  for (i = outputIndex; i < neuronCount; i++) {
		  //System.out.println(ideal[i - outputIndex]);
	   error[i] = ideal[i - outputIndex] - fire[i];
	   globalError += error[i] * error[i];
	  // System.out.println("The Global Error is "+globalError);
	   errorDelta[i] = error[i] * fire[i] * (1 - fire[i]);
	  // System.out.println("The Output Error is: "+errorDelta[i]);
	  }

	  // hidden layer errors
	  int winx = inputCount * hiddenCount;
      //System.out.println(winx);
	  for (i = outputIndex; i < neuronCount; i++) {
	   for (j = hiddenIndex; j < outputIndex; j++) {
	    accMatrixDelta[winx] += errorDelta[i] * fire[j];
	    error[j] += matrix[winx] * errorDelta[i];
	    winx++;
	   }
	   accThresholdDelta[i] += errorDelta[i];
	  }

	  // hidden layer deltas                     * Hidden LAyer Error Calculation
	  for (i = hiddenIndex; i < outputIndex; i++) {
	   errorDelta[i] = error[i] * fire[i] * (1 - fire[i]);
	  // System.out.println("The Error for the Hidden Layer Neuron "+(i+1)+" is "+errorDelta[i]);
	  }

	  // input layer errors
	  winx = 0; // offset into weight array
	  for (i = hiddenIndex; i < outputIndex; i++) {
	   for (j = 0; j < hiddenIndex; j++) {
	    accMatrixDelta[winx] += errorDelta[i] * fire[j];
	    error[j] += matrix[winx] * errorDelta[i];
	    winx++;
	   }
	   accThresholdDelta[i] += errorDelta[i];
	  }
	 // for(j = 0; j < hiddenIndex; j++)
	 // System.out.println("The Error for the Input Layer Neuron "+(j+1)+" is "+error[j]);
	}

	 /**
	  * Modify the weight matrix and thresholds based on the last call to
	  * calcError.
	  */
	 public void learn() {
	  int i;

	  // process the matrix
	  for (i = 0; i < matrix.length; i++) {
	   matrixDelta[i] = (learnRate * accMatrixDelta[i]) + (momentum * matrixDelta[i]);
	   matrix[i] += matrixDelta[i];
	   //System.out.println("The Updated Weight "+(i+1)+" is "+matrix[i]);
	   accMatrixDelta[i] = 0;
	  }

	  // process the thresholds
	  for (i = inputCount; i < neuronCount; i++) {
	   thresholdDelta[i] = learnRate * accThresholdDelta[i] + (momentum * thresholdDelta[i]);
	   thresholds[i] += thresholdDelta[i];
	 //  System.out.println("The Updated Threshold "+(i+1)+" is "+thresholds[i]);
	   accThresholdDelta[i] = 0;
	  }
	 }

	 /**
	  * Reset the weight matrix and the thresholds.
	  */
	 public void reset() {
	  int i;

	  for (i = 0; i < neuronCount; i++) {
	   thresholds[i] = 0.5-(Math.random());
	   thresholdDelta[i] = 0;
	   accThresholdDelta[i] = 0;
	   //System.out.println(thresholds[i]);
	  }
	 // System.out.println("The Weights are ");
	  for (i = 0; i < matrix.length; i++) {
		 // double raw=(Math.random());
		 // System.out.println("raw :"+raw);
	   matrix[i] = 0.5-(Math.random());
	   matrixDelta[i] = 0;
	   accMatrixDelta[i] = 0;
	  }
	 }
	 public double []learnedSystem(double input[]) {
		  int i, j;
		  final int hiddenIndex = inputCount;
		  final int outIndex = inputCount + hiddenCount;

		  for (i = 0; i < inputCount; i++) {
		   fire[i] = input[i];
		  }

		  // first layer
		  int inx = 0;

		  for (i = hiddenIndex; i < outIndex; i++) {
		   double sum = thresholds[i];                  //Constant Threshold =0.5

		   for (j = 0; j < inputCount; j++) {
		    sum += fire[j] * matrix[inx++];
		   }
		   fire[i] = threshold(sum);
		  // System.out.println("The Output for the Hidden Layer Neuron "+(i+1)+" is: "+fire[i]);
		  }

		  // hidden layer

		  double result[] = new double[outputCount];

		  for (i = outIndex; i < neuronCount; i++) {
		   double sum = thresholds[i];

		   //System.out.println("Adding Thresholds and Calculating output!!");
		   for (j = hiddenIndex; j < outIndex; j++) {
		    sum += fire[j] * matrix[inx++];
		   }
		   fire[i] = threshold(sum);      //Calling for calculating Sigmond Function;
		  // System.out.println(i);
		   result[i-outIndex] = fire[i];
		   //System.out.println("The Actual Output is :"+result[i-outIndex]);
		  }

		  return result;
		 }
}
