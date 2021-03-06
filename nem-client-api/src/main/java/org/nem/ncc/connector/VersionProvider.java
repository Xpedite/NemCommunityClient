package org.nem.ncc.connector;

import org.nem.core.connect.ErrorResponseDeserializerUnion;
import org.nem.core.connect.HttpErrorResponseDeserializerUnionStrategy;
import org.nem.core.connect.HttpMethodClient;
import org.nem.core.metadata.MetaDataFactory;
import org.nem.core.node.NodeVersion;
import org.nem.core.serialization.Deserializer;
import org.nem.core.time.SystemTimeProvider;
import org.nem.core.utils.ExceptionUtils;

import java.net.URL;
import java.util.logging.Logger;

/**
 * Helper class for getting version information.
 */
public class VersionProvider {
	private static final Logger LOGGER = Logger.getLogger(VersionProvider.class.getName());

	private final static String VERSION_PROVIDER_URL = "http://bob.nem.ninja/version.json";
	private final static String VERSION_FLAVOR = "stable";

	private final HttpMethodClient<ErrorResponseDeserializerUnion> httpClient;

	/**
	 * Creates a new version provider.
	 *
	 * @param httpClient The http method client to used.
	 */
	public VersionProvider(final HttpMethodClient<ErrorResponseDeserializerUnion> httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Gets the local NEM version.
	 *
	 * @return The local NEM version.
	 */
	public NodeVersion getLocalVersion() {
		final String localVersion = MetaDataFactory.loadApplicationMetaData(VersionProvider.class, new SystemTimeProvider()).getVersion();
		return NodeVersion.parse(localVersion);
	}

	/**
	 * Gets the latest NEM version.
	 *
	 * @return The latest NEM version.
	 */
	public NodeVersion getLatestVersion() {
		try {
			final URL url = ExceptionUtils.propagate(() -> new URL(VERSION_PROVIDER_URL));
			return this.httpClient.get(url, new HttpErrorResponseDeserializerUnionStrategy(null))
					.getFuture()
					.thenApply(union -> {
						final Deserializer deserializer = union.getDeserializer();
						return NodeVersion.parse(deserializer.readString(VERSION_FLAVOR));
					}).join();
		} catch (final Exception e) {
			// if there's an error retrieving the latest version, ignore it because
			// we still want nem monitor to be able to boot up and run
			LOGGER.warning(String.format("unable to determine latest version: %s", e));
			return NodeVersion.ZERO;
		}
	}
}
