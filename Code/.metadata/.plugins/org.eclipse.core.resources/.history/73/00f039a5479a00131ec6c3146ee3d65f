package decisionTrees;

import neuralNetwork.INeuralNetwork;

public class MetaclassNode {
	private char[] classes;
	private INeuralNetwork network;
	private boolean isLeafNode;
	
	private MetaclassNode leftChild, rightChild;
	
	public MetaclassNode(char[] classes, INeuralNetwork network){
		this.classes = classes;
		this.network = network;
	}

	public boolean isLeafNode() {
		return isLeafNode;
	}

	public void setLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}

	public char[] getClasses() {
		return classes;
	}

	public INeuralNetwork getNetwork() {
		return network;
	}

	public MetaclassNode getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(MetaclassNode leftChild) {
		this.leftChild = leftChild;
	}

	public MetaclassNode getRightChild() {
		return rightChild;
	}

	public void setRightChild(MetaclassNode rightChild) {
		this.rightChild = rightChild;
	}
}
