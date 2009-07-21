package org.arabidopsis.ahocorasick;

import gnu.trove.TIntHashSet;


/**
   A state represents an element in the Aho-Corasick tree.
 */


class State {


    // Arbitrarily chosen constant.  If this state ends up getting
    // deeper than THRESHOLD_TO_USE_SPARSE, then we switch over to a
    // sparse edge representation.  I did a few tests, and there's a
    // local minima here.  We may want to choose a more sophisticated
    // strategy.
    private static final int THRESHOLD_TO_USE_SPARSE = 3;

    private TinyByteMap<State> edgeList;
    private State fail;
    private TIntHashSet outputs;

    public State() {
    }

    TinyByteMap<State> getEdgeList(){
      if(this.edgeList == null)
        this.edgeList = new TinyByteMap();

      return edgeList;
    }


    public State extend(byte b) {
	if (this.getEdgeList().get(b) != null)
	    return this.getEdgeList().get(b);
	State nextState = new State();
	this.getEdgeList().put(b, nextState);
	return nextState;
    }


    public State extendAll(byte[] bytes) {
	State state = this;
	for (int i = 0; i < bytes.length; i++) {
	    if (state.getEdgeList().get(bytes[i]) != null)
		state = state.getEdgeList().get(bytes[i]);
	    else
		state = state.extend(bytes[i]);
	}
	return state;
    }


    /**
       Returns the size of the tree rooted at this State.  Note: do
       not call this if there are loops in the edgelist graph, such as
       those introduced by AhoCorasick.prepare().
     */
    public int size() {
	byte[] keys = keys();
	int result = 1;
	for (int i = 0; i < keys.length; i++)
	    result += getEdgeList().get(keys[i]).size();
	return result;
    }


    public State get(byte b) {
	return this.getEdgeList().get(b);
    }


    public void put(byte b, State s) {
	this.getEdgeList().put(b, s);
    }

    public byte[] keys() {
      if(edgeList == null) return TinyByteMap.EMPTY; 
      else return this.getEdgeList().keys();
    }


    public State getFail() {
	return this.fail;
    }


    public void setFail(State f) {
	this.fail = f;
    }


    public void addOutput(int o) {
	this.getOutputs().add(o);
    }


    public TIntHashSet getOutputs() {
      if(this.outputs == null){
        this.outputs = new TIntHashSet(1);
      }
	return this.outputs;
    }
}
