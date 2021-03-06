package org.nem.ncc.storable.entity;

import org.nem.core.serialization.*;
import org.nem.core.utils.StringUtils;

/**
 * Represents a name for a storable entity.
 */
public abstract class StorableEntityName<TDerived extends StorableEntityName> {
	protected final String name;
	protected final String label;
	private final Class<TDerived> derivedClass;

	/**
	 * Creates a storable entity name.
	 *
	 * @param name The name.
	 */
	protected StorableEntityName(final String name) {
		this(name, "name", null);
	}

	/**
	 * Creates a storable entity name.
	 *
	 * @param name The name.
	 * @param label The label.
	 */
	protected StorableEntityName(final String name, final String label) {
		this(name, label, null);
	}

	/**
	 * Creates a storable entity name.
	 *
	 * @param name The name.
	 * @param label The label.
	 * @param derivedClass The derived class.
	 */
	protected StorableEntityName(
			final String name,
			final String label,
			final Class<TDerived> derivedClass) {
		if (StringUtils.isNullOrWhitespace(name)) {
			throw new IllegalArgumentException("name must be non-whitespace");
		}

		if (StringUtils.isNullOrWhitespace(label)) {
			throw new IllegalArgumentException("label must be non-whitespace");
		}

		this.name = name;
		this.label = label;
		this.derivedClass = derivedClass;
	}

	/**
	 * Creates a storable entity name.
	 *
	 * @param deserializer The deserializer.
	 * @param label The label to read from.
	 * @param derivedClass The derived class.
	 */
	protected StorableEntityName(
			final Deserializer deserializer,
			final String label,
			final Class<TDerived> derivedClass) {
		if (StringUtils.isNullOrWhitespace(label)) {
			throw new IllegalArgumentException("label must be non-whitespace");
		}

		this.label = label;
		this.name = deserializer.readString(this.label);
		this.derivedClass = derivedClass;
	}

	/**
	 * Gets the label for serialization.
	 *
	 * @return The label.
	 */
	public String getLabel() {
		return this.label;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		final Class clazz = null == this.derivedClass ? StorableEntityName.class : this.derivedClass;
		if (!clazz.isInstance(obj)) {
			return false;
		}

		final StorableEntityName rhs = (StorableEntityName)obj;
		return this.name.equals(rhs.name);
	}

	@Override
	public String toString() {
		return this.name;
	}

	//region inline serialization

	/**
	 * Writes a storable entity name object.
	 *
	 * @param serializer The serializer to use.
	 * @param label The optional label.
	 * @param name The object.
	 */
	public static void writeTo(final Serializer serializer, final String label, final StorableEntityName name) {
		serializer.writeString(label, name.toString());
	}

	//endregion
}
