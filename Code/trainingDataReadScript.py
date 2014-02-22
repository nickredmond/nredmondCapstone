import pickle, gzip, numpy
from PIL import Image

width = 28
height = 28
trainingDataPath = 'C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/metadataFiles/trainingMetadata.txt'
testDataPath = 'C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/metadataFiles/testMetadata.txt'
validationDataPath = 'C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/metadataFiles/validationMetadata.txt'

def writeImages(dataSet, startIndex, numberImages, filepath, dataFilepath):
	imageData = dataSet[0]
	imageLabels = dataSet[1]
	
	dataFile = open(dataFilepath, 'a+')	
	fileLines = list(dataFile)
	currentImgNumber = int(fileLines[0].replace('\n', '')) if len(fileLines) > 0 else 1
	dataFile.close()

	for i in range(startIndex, startIndex + numberImages):
		nextTrainingInput = imageData[i]
		nextImg = Image.new("RGB", (width, height))
		
		for x in range(width):
			for y in range(height):
				index = (x * width) + y
				nextRGB = int(nextTrainingInput[index] * 255)
				nextImg.putpixel((y,x), (nextRGB, nextRGB, nextRGB))

		filename = filepath + str(currentImgNumber) + '.bmp'
		nextImg.save(filename, 'bmp')
		
		nextLabel = str(imageLabels[i])
		nextDataLine = filename + ' ' + nextLabel
		
		fileLines.append(nextDataLine + '\n')
		
		currentImgNumber += 1
		
	dataFile = open(dataFilepath, 'w')
	fileLines[0] = str(currentImgNumber) + '\n'
	
	for line in fileLines:
		dataFile.write(line)
		
	dataFile.close()
	print('successful execution')
	raw_input()

mnistFile = gzip.open('C:/Users/nredmond/Downloads/mnist.pkl.gz', 'rb')
training_set, validation_set, test_set = pickle.load(mnistFile)
mnistFile.close()

imgPath = 'C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Vendor/handwrittenData/'
trainingDir = 'trainingSet/'
testDir = 'testSet/'
validationDir = 'validationSet/'

writeImages(validation_set, 0, 50, imgPath + validationDir, validationDataPath)