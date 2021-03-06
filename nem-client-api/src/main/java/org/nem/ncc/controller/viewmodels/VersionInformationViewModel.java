package org.nem.ncc.controller.viewmodels;

import org.nem.core.node.NodeVersion;
import org.nem.core.serialization.SerializableEntity;
import org.nem.core.serialization.Serializer;

public class VersionInformationViewModel implements SerializableEntity {
	final NodeVersion localVersion;
	final NodeVersion latestVersion;

	public VersionInformationViewModel(final NodeVersion localVersion, final NodeVersion latestVersion) {
		this.localVersion = localVersion;
		this.latestVersion = latestVersion;
	}

	@Override
	public void serialize(final Serializer serializer) {
		NodeVersion.writeTo(serializer, "local", this.localVersion);
		NodeVersion.writeTo(serializer, "latest", this.latestVersion);
	}
}
