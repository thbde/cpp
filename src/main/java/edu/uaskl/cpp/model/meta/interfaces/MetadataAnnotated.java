package edu.uaskl.cpp.model.meta.interfaces;

public interface MetadataAnnotated<T extends Metadata> {
	public T getMetadata();
	public void setMetadata(T metadata);
}
