/*
 * 
 */
package com.ttth.teamcaring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1ServiceProvider;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2ServiceProvider;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.domain.SocialUserConnection;

/**
 * The Class CustomSocialUsersConnectionRepositoryIntTest.
 *
 * @author Dai Mai
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Transactional
public class CustomSocialUsersConnectionRepositoryIntTest {

    /** The connection factory registry. */
    private ConnectionFactoryRegistry             connectionFactoryRegistry;

    /** The connection factory. */
    private TestFacebookConnectionFactory         connectionFactory;

    /** The users connection repository. */
    private CustomSocialUsersConnectionRepository usersConnectionRepository;

    /** The connection repository. */
    private ConnectionRepository                  connectionRepository;

    /** The social user connection repository. */
    @Autowired
    private SocialUserConnectionRepository        socialUserConnectionRepository;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        socialUserConnectionRepository.deleteAll();

        connectionFactoryRegistry = new ConnectionFactoryRegistry();
        connectionFactory = new TestFacebookConnectionFactory();
        connectionFactoryRegistry.addConnectionFactory(connectionFactory);
        usersConnectionRepository = new CustomSocialUsersConnectionRepository(
                socialUserConnectionRepository, connectionFactoryRegistry);
        connectionRepository = usersConnectionRepository.createConnectionRepository("1");
    }

    /**
     * Find user id with connection.
     */
    @Test
    public void findUserIdWithConnection() {
        insertFacebookConnection();
        List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(
                connectionRepository.getPrimaryConnection(TestFacebookApi.class));
        assertEquals("1", userIds.get(0));
    }

    /**
     * Find user id with connection no such connection.
     */
    @Test
    public void findUserIdWithConnectionNoSuchConnection() {
        Connection<TestFacebookApi> connection = connectionFactory
                .createConnection(new AccessGrant("12345"));
        assertEquals(0, usersConnectionRepository.findUserIdsWithConnection(connection).size());
    }

    /**
     * Find user id with connection multiple connections to same provider user.
     */
    @Test
    public void findUserIdWithConnectionMultipleConnectionsToSameProviderUser() {
        insertFacebookConnection();
        insertFacebookConnectionSameFacebookUser();
        List<String> localUserIds = usersConnectionRepository.findUserIdsWithConnection(
                connectionRepository.getPrimaryConnection(TestFacebookApi.class));
        assertEquals(2, localUserIds.size());
        assertEquals("1", localUserIds.get(0));
        assertEquals("2", localUserIds.get(1));
    }

    /**
     * Find user ids connected to.
     */
    @Test
    public void findUserIdsConnectedTo() {
        insertFacebookConnection();
        insertFacebookConnection3();
        Set<String> localUserIds = usersConnectionRepository.findUserIdsConnectedTo("facebook",
                new HashSet<>(Arrays.asList("9", "11")));
        assertEquals(2, localUserIds.size());
        assertTrue(localUserIds.contains("1"));
        assertTrue(localUserIds.contains("2"));
    }

    /**
     * Find all connections.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findAllConnections() {
        connectionFactoryRegistry.addConnectionFactory(new TestTwitterConnectionFactory());
        insertTwitterConnection();
        insertFacebookConnection();
        MultiValueMap<String, Connection<?>> connections = connectionRepository
                .findAllConnections();
        assertEquals(2, connections.size());
        Connection<TestFacebookApi> facebook = (Connection<TestFacebookApi>) connections
                .getFirst("facebook");
        assertFacebookConnection(facebook);
        Connection<TestTwitterApi> twitter = (Connection<TestTwitterApi>) connections
                .getFirst("twitter");
        assertTwitterConnection(twitter);
    }

    /**
     * Find all connections multiple connection results.
     */
    @Test
    public void findAllConnectionsMultipleConnectionResults() {
        connectionFactoryRegistry.addConnectionFactory(new TestTwitterConnectionFactory());
        insertTwitterConnection();
        insertFacebookConnection();
        insertFacebookConnection2();
        MultiValueMap<String, Connection<?>> connections = connectionRepository
                .findAllConnections();
        assertEquals(2, connections.size());
        assertEquals(2, connections.get("facebook").size());
        assertEquals(1, connections.get("twitter").size());
    }

    /**
     * Find all connections empty result.
     */
    @Test
    public void findAllConnectionsEmptyResult() {
        connectionFactoryRegistry.addConnectionFactory(new TestTwitterConnectionFactory());
        MultiValueMap<String, Connection<?>> connections = connectionRepository
                .findAllConnections();
        assertEquals(2, connections.size());
        assertEquals(0, connections.get("facebook").size());
        assertEquals(0, connections.get("twitter").size());
    }

    /**
     * No such connection factory.
     */
    @Test(expected = IllegalArgumentException.class)
    public void noSuchConnectionFactory() {
        insertTwitterConnection();
        connectionRepository.findAllConnections();
    }

    /**
     * Find connections by provider id.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findConnectionsByProviderId() {
        connectionFactoryRegistry.addConnectionFactory(new TestTwitterConnectionFactory());
        insertTwitterConnection();
        List<Connection<?>> connections = connectionRepository.findConnections("twitter");
        assertEquals(1, connections.size());
        assertTwitterConnection((Connection<TestTwitterApi>) connections.get(0));
    }

    /**
     * Find connections by provider id empty result.
     */
    @Test
    public void findConnectionsByProviderIdEmptyResult() {
        assertTrue(connectionRepository.findConnections("facebook").isEmpty());
    }

    /**
     * Find connections by api.
     */
    @Test
    public void findConnectionsByApi() {
        insertFacebookConnection();
        insertFacebookConnection2();
        List<Connection<TestFacebookApi>> connections = connectionRepository
                .findConnections(TestFacebookApi.class);
        assertEquals(2, connections.size());
        assertFacebookConnection(connections.get(0));
    }

    /**
     * Find connections by api empty result.
     */
    @Test
    public void findConnectionsByApiEmptyResult() {
        assertTrue(connectionRepository.findConnections(TestFacebookApi.class).isEmpty());
    }

    /**
     * Find connections to users.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findConnectionsToUsers() {
        connectionFactoryRegistry.addConnectionFactory(new TestTwitterConnectionFactory());
        insertTwitterConnection();
        insertFacebookConnection();
        insertFacebookConnection2();
        MultiValueMap<String, String> providerUsers = new LinkedMultiValueMap<>();
        providerUsers.add("facebook", "10");
        providerUsers.add("facebook", "9");
        providerUsers.add("twitter", "1");
        MultiValueMap<String, Connection<?>> connectionsForUsers = connectionRepository
                .findConnectionsToUsers(providerUsers);
        assertEquals(2, connectionsForUsers.size());
        String providerId = connectionsForUsers.getFirst("facebook").getKey().getProviderUserId();
        assertTrue("10".equals(providerId) || "9".equals(providerId));
        assertFacebookConnection((Connection<TestFacebookApi>) connectionRepository
                .getConnection(new ConnectionKey("facebook", "9")));
        assertTwitterConnection(
                (Connection<TestTwitterApi>) connectionsForUsers.getFirst("twitter"));
    }

    /**
     * Find connections to users empty result.
     */
    @Test
    public void findConnectionsToUsersEmptyResult() {
        MultiValueMap<String, String> providerUsers = new LinkedMultiValueMap<>();
        providerUsers.add("facebook", "1");
        assertTrue(connectionRepository.findConnectionsToUsers(providerUsers).isEmpty());
    }

    /**
     * Find connections to users empty input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void findConnectionsToUsersEmptyInput() {
        MultiValueMap<String, String> providerUsers = new LinkedMultiValueMap<>();
        connectionRepository.findConnectionsToUsers(providerUsers);
    }

    /**
     * Find connection by key.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findConnectionByKey() {
        insertFacebookConnection();
        assertFacebookConnection((Connection<TestFacebookApi>) connectionRepository
                .getConnection(new ConnectionKey("facebook", "9")));
    }

    /**
     * Find connection by key no such connection.
     */
    @Test(expected = NoSuchConnectionException.class)
    public void findConnectionByKeyNoSuchConnection() {
        connectionRepository.getConnection(new ConnectionKey("facebook", "bogus"));
    }

    /**
     * Find connection by api to user.
     */
    @Test
    public void findConnectionByApiToUser() {
        insertFacebookConnection();
        insertFacebookConnection2();
        assertFacebookConnection(connectionRepository.getConnection(TestFacebookApi.class, "9"));
        assertEquals("10", connectionRepository.getConnection(TestFacebookApi.class, "10").getKey()
                .getProviderUserId());
    }

    /**
     * Find connection by api to user no such connection.
     */
    @Test(expected = NoSuchConnectionException.class)
    public void findConnectionByApiToUserNoSuchConnection() {
        assertFacebookConnection(connectionRepository.getConnection(TestFacebookApi.class, "9"));
    }

    /**
     * Find primary connection.
     */
    @Test
    public void findPrimaryConnection() {
        insertFacebookConnection();
        assertFacebookConnection(connectionRepository.getPrimaryConnection(TestFacebookApi.class));
    }

    /**
     * Find primary connection select from multiple by rank.
     */
    @Test
    public void findPrimaryConnectionSelectFromMultipleByRank() {
        insertFacebookConnection2();
        insertFacebookConnection();
        assertFacebookConnection(connectionRepository.getPrimaryConnection(TestFacebookApi.class));
    }

    /**
     * Find primary connection not connected.
     */
    @Test(expected = NotConnectedException.class)
    public void findPrimaryConnectionNotConnected() {
        connectionRepository.getPrimaryConnection(TestFacebookApi.class);
    }

    /**
     * Removes the connections.
     */
    @Test
    public void removeConnections() {
        SocialUserConnection facebookConnection = insertFacebookConnection();
        insertFacebookConnection2();
        assertThat(socialUserConnectionRepository.findOne(facebookConnection.getId())).isNotNull();
        connectionRepository.removeConnections("facebook");
        assertThat(socialUserConnectionRepository.findOne(facebookConnection.getId())).isNull();
    }

    /**
     * Removes the connections to provider no op.
     */
    @Test
    public void removeConnectionsToProviderNoOp() {
        connectionRepository.removeConnections("twitter");
    }

    /**
     * Removes the connection.
     */
    @Test
    public void removeConnection() {
        SocialUserConnection facebookConnection = insertFacebookConnection();
        assertThat(socialUserConnectionRepository.findOne(facebookConnection.getId())).isNotNull();
        connectionRepository.removeConnection(new ConnectionKey("facebook", "9"));
        assertThat(socialUserConnectionRepository.findOne(facebookConnection.getId())).isNull();
    }

    /**
     * Removes the connection no op.
     */
    @Test
    public void removeConnectionNoOp() {
        connectionRepository.removeConnection(new ConnectionKey("facebook", "1"));
    }

    /**
     * Adds the connection.
     */
    @Test
    public void addConnection() {
        Connection<TestFacebookApi> connection = connectionFactory
                .createConnection(new AccessGrant("123456789", null, "987654321", 3600L));
        connectionRepository.addConnection(connection);
        Connection<TestFacebookApi> restoredConnection = connectionRepository
                .getPrimaryConnection(TestFacebookApi.class);
        assertEquals(connection, restoredConnection);
        assertNewConnection(restoredConnection);
    }

    /**
     * Adds the connection duplicate.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void addConnectionDuplicate() {
        Connection<TestFacebookApi> connection = connectionFactory
                .createConnection(new AccessGrant("123456789", null, "987654321", 3600L));
        connectionRepository.addConnection(connection);
        connectionRepository.addConnection(connection);
        socialUserConnectionRepository.flush();
    }

    /**
     * Update connection profile fields.
     */
    @Test
    public void updateConnectionProfileFields() {
        connectionFactoryRegistry.addConnectionFactory(new TestTwitterConnectionFactory());
        insertTwitterConnection();
        Connection<TestTwitterApi> twitter = connectionRepository
                .getPrimaryConnection(TestTwitterApi.class);
        assertEquals("http://twitter.com/kdonald/picture", twitter.getImageUrl());
        twitter.sync();
        assertEquals("http://twitter.com/kdonald/a_new_picture", twitter.getImageUrl());
        connectionRepository.updateConnection(twitter);
        Connection<TestTwitterApi> twitter2 = connectionRepository
                .getPrimaryConnection(TestTwitterApi.class);
        assertEquals("http://twitter.com/kdonald/a_new_picture", twitter2.getImageUrl());
    }

    /**
     * Update connection access fields.
     */
    @Test
    public void updateConnectionAccessFields() {
        insertFacebookConnection();
        Connection<TestFacebookApi> facebook = connectionRepository
                .getPrimaryConnection(TestFacebookApi.class);
        assertEquals("234567890", facebook.getApi().getAccessToken());
        facebook.refresh();
        connectionRepository.updateConnection(facebook);
        Connection<TestFacebookApi> facebook2 = connectionRepository
                .getPrimaryConnection(TestFacebookApi.class);
        assertEquals("765432109", facebook2.getApi().getAccessToken());
        ConnectionData data = facebook.createData();
        assertEquals("654321098", data.getRefreshToken());
    }

    /**
     * Find primary connection after remove.
     */
    @Test
    public void findPrimaryConnection_afterRemove() {
        insertFacebookConnection();
        insertFacebookConnection2();
        // 9 is the providerUserId of the first Facebook connection
        connectionRepository.removeConnection(new ConnectionKey("facebook", "9"));
        assertEquals(1, connectionRepository.findConnections(TestFacebookApi.class).size());
        assertNotNull(connectionRepository.findPrimaryConnection(TestFacebookApi.class));
    }

    /**
     * Insert twitter connection.
     *
     * @return the social user connection
     */
    private SocialUserConnection insertTwitterConnection() {
        return createExistingSocialUserConnection("1", "twitter", "1", 1L, "@kdonald",
                "http://twitter.com/kdonald", "http://twitter.com/kdonald/picture", "123456789",
                "987654321", null, null);
    }

    /**
     * Insert facebook connection.
     *
     * @return the social user connection
     */
    private SocialUserConnection insertFacebookConnection() {
        return createExistingSocialUserConnection("1", "facebook", "9", 1L, null, null, null,
                "234567890", null, "345678901", System.currentTimeMillis() + 3600000);
    }

    /**
     * Insert facebook connection 2.
     *
     * @return the social user connection
     */
    private SocialUserConnection insertFacebookConnection2() {
        return createExistingSocialUserConnection("1", "facebook", "10", 2L, null, null, null,
                "456789012", null, "56789012", System.currentTimeMillis() + 3600000);
    }

    /**
     * Insert facebook connection 3.
     *
     * @return the social user connection
     */
    private SocialUserConnection insertFacebookConnection3() {
        return createExistingSocialUserConnection("2", "facebook", "11", 2L, null, null, null,
                "456789012", null, "56789012", System.currentTimeMillis() + 3600000);
    }

    /**
     * Insert facebook connection same facebook user.
     *
     * @return the social user connection
     */
    private SocialUserConnection insertFacebookConnectionSameFacebookUser() {
        return createExistingSocialUserConnection("2", "facebook", "9", 1L, null, null, null,
                "234567890", null, "345678901", System.currentTimeMillis() + 3600000);
    }

    /**
     * Creates the existing social user connection.
     *
     * @param userId
     *        the user id
     * @param providerId
     *        the provider id
     * @param providerUserId
     *        the provider user id
     * @param rank
     *        the rank
     * @param displayName
     *        the display name
     * @param profileURL
     *        the profile URL
     * @param imageURL
     *        the image URL
     * @param accessToken
     *        the access token
     * @param secret
     *        the secret
     * @param refreshToken
     *        the refresh token
     * @param expireTime
     *        the expire time
     * @return the social user connection
     */
    private SocialUserConnection createExistingSocialUserConnection(String userId,
            String providerId, String providerUserId, Long rank, String displayName,
            String profileURL, String imageURL, String accessToken, String secret,
            String refreshToken, Long expireTime) {
        SocialUserConnection socialUserConnectionToSabe = new SocialUserConnection(userId,
                providerId, providerUserId, rank, displayName, profileURL, imageURL, accessToken,
                secret, refreshToken, expireTime);
        return socialUserConnectionRepository.save(socialUserConnectionToSabe);
    }

    /**
     * Assert new connection.
     *
     * @param connection
     *        the connection
     */
    private void assertNewConnection(Connection<TestFacebookApi> connection) {
        assertEquals("facebook", connection.getKey().getProviderId());
        assertEquals("9", connection.getKey().getProviderUserId());
        assertEquals("Keith Donald", connection.getDisplayName());
        assertEquals("http://facebook.com/keith.donald", connection.getProfileUrl());
        assertEquals("http://facebook.com/keith.donald/picture", connection.getImageUrl());
        assertTrue(connection.test());
        TestFacebookApi api = connection.getApi();
        assertNotNull(api);
        assertEquals("123456789", api.getAccessToken());
        assertEquals("123456789", connection.createData().getAccessToken());
        assertEquals("987654321", connection.createData().getRefreshToken());
    }

    /**
     * Assert twitter connection.
     *
     * @param twitter
     *        the twitter
     */
    private void assertTwitterConnection(Connection<TestTwitterApi> twitter) {
        assertEquals(new ConnectionKey("twitter", "1"), twitter.getKey());
        assertEquals("@kdonald", twitter.getDisplayName());
        assertEquals("http://twitter.com/kdonald", twitter.getProfileUrl());
        assertEquals("http://twitter.com/kdonald/picture", twitter.getImageUrl());
        TestTwitterApi twitterApi = twitter.getApi();
        assertEquals("123456789", twitterApi.getAccessToken());
        assertEquals("987654321", twitterApi.getSecret());
        twitter.sync();
        assertEquals("http://twitter.com/kdonald/a_new_picture", twitter.getImageUrl());
    }

    /**
     * Assert facebook connection.
     *
     * @param facebook
     *        the facebook
     */
    private void assertFacebookConnection(Connection<TestFacebookApi> facebook) {
        assertEquals(new ConnectionKey("facebook", "9"), facebook.getKey());
        assertEquals(null, facebook.getDisplayName());
        assertEquals(null, facebook.getProfileUrl());
        assertEquals(null, facebook.getImageUrl());
        TestFacebookApi facebookApi = facebook.getApi();
        assertEquals("234567890", facebookApi.getAccessToken());
        facebook.sync();
        assertEquals("Keith Donald", facebook.getDisplayName());
        assertEquals("http://facebook.com/keith.donald", facebook.getProfileUrl());
        assertEquals("http://facebook.com/keith.donald/picture", facebook.getImageUrl());
    }

    /**
     * A factory for creating TestFacebookConnection objects.
     */
    // test facebook provider
    private static class TestFacebookConnectionFactory
            extends OAuth2ConnectionFactory<TestFacebookApi> {

        /**
         * Instantiates a new test facebook connection factory.
         */
        public TestFacebookConnectionFactory() {
            super("facebook", new TestFacebookServiceProvider(), new TestFacebookApiAdapter());
        }
    }

    /**
     * The Class TestFacebookServiceProvider.
     *
     * @author Dai Mai
     */
    private static class TestFacebookServiceProvider
            implements OAuth2ServiceProvider<TestFacebookApi> {

        /*
         * (non-Javadoc)
         * 
         * @see org.springframework.social.oauth2.OAuth2ServiceProvider#
         * getOAuthOperations()
         */
        public OAuth2Operations getOAuthOperations() {
            return new OAuth2Operations() {

                public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters params) {
                    return null;
                }

                public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters params) {
                    return null;
                }

                public String buildAuthorizeUrl(OAuth2Parameters params) {
                    return null;
                }

                public String buildAuthenticateUrl(OAuth2Parameters params) {
                    return null;
                }

                public AccessGrant exchangeForAccess(String authorizationGrant, String redirectUri,
                        MultiValueMap<String, String> additionalParameters) {
                    return null;
                }

                public AccessGrant exchangeCredentialsForAccess(String username, String password,
                        MultiValueMap<String, String> additionalParameters) {
                    return null;
                }

                public AccessGrant refreshAccess(String refreshToken,
                        MultiValueMap<String, String> additionalParameters) {
                    return new AccessGrant("765432109", "read", "654321098", 3600L);
                }

                @Deprecated
                public AccessGrant refreshAccess(String refreshToken, String scope,
                        MultiValueMap<String, String> additionalParameters) {
                    return new AccessGrant("765432109", "read", "654321098", 3600L);
                }

                public AccessGrant authenticateClient() {
                    return null;
                }

                public AccessGrant authenticateClient(String scope) {
                    return null;
                }
            };
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.oauth2.OAuth2ServiceProvider#getApi(java.
         * lang.String)
         */
        public TestFacebookApi getApi(final String accessToken) {
            return () -> accessToken;
        }

    }

    /**
     * The Interface TestFacebookApi.
     *
     * @author Dai Mai
     */
    public interface TestFacebookApi {

        /**
         * Gets the access token.
         *
         * @return the access token
         */
        String getAccessToken();

    }

    /**
     * The Class TestFacebookApiAdapter.
     *
     * @author Dai Mai
     */
    private static class TestFacebookApiAdapter implements ApiAdapter<TestFacebookApi> {

        /** The account id. */
        private String accountId         = "9";

        /** The name. */
        private String name              = "Keith Donald";

        /** The profile url. */
        private String profileUrl        = "http://facebook.com/keith.donald";

        /** The profile picture url. */
        private String profilePictureUrl = "http://facebook.com/keith.donald/picture";

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#test(java.lang.Object)
         */
        public boolean test(TestFacebookApi api) {
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#setConnectionValues(
         * java.lang.Object,
         * org.springframework.social.connect.ConnectionValues)
         */
        public void setConnectionValues(TestFacebookApi api, ConnectionValues values) {
            values.setProviderUserId(accountId);
            values.setDisplayName(name);
            values.setProfileUrl(profileUrl);
            values.setImageUrl(profilePictureUrl);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#fetchUserProfile(java.
         * lang.Object)
         */
        public UserProfile fetchUserProfile(TestFacebookApi api) {
            return new UserProfileBuilder().setName(name).setEmail("keith@interface21.com")
                    .setUsername("Keith.Donald").build();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#updateStatus(java.lang.
         * Object, java.lang.String)
         */
        public void updateStatus(TestFacebookApi api, String message) {

        }
    }

    /**
     * A factory for creating TestTwitterConnection objects.
     */
    // test twitter provider
    private static class TestTwitterConnectionFactory
            extends OAuth1ConnectionFactory<TestTwitterApi> {

        /**
         * Instantiates a new test twitter connection factory.
         */
        public TestTwitterConnectionFactory() {
            super("twitter", new TestTwitterServiceProvider(), new TestTwitterApiAdapter());
        }
    }

    /**
     * The Class TestTwitterServiceProvider.
     *
     * @author Dai Mai
     */
    private static class TestTwitterServiceProvider
            implements OAuth1ServiceProvider<TestTwitterApi> {

        /*
         * (non-Javadoc)
         * 
         * @see org.springframework.social.oauth1.OAuth1ServiceProvider#
         * getOAuthOperations()
         */
        public OAuth1Operations getOAuthOperations() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.oauth1.OAuth1ServiceProvider#getApi(java.
         * lang.String, java.lang.String)
         */
        public TestTwitterApi getApi(final String accessToken, final String secret) {
            return new TestTwitterApi() {

                public String getAccessToken() {
                    return accessToken;
                }

                public String getSecret() {
                    return secret;
                }
            };
        }

    }

    /**
     * The Interface TestTwitterApi.
     *
     * @author Dai Mai
     */
    public interface TestTwitterApi {

        /**
         * Gets the access token.
         *
         * @return the access token
         */
        String getAccessToken();

        /**
         * Gets the secret.
         *
         * @return the secret
         */
        String getSecret();

    }

    /**
     * The Class TestTwitterApiAdapter.
     *
     * @author Dai Mai
     */
    private static class TestTwitterApiAdapter implements ApiAdapter<TestTwitterApi> {

        /** The account id. */
        private String accountId         = "1";

        /** The name. */
        private String name              = "@kdonald";

        /** The profile url. */
        private String profileUrl        = "http://twitter.com/kdonald";

        /** The profile picture url. */
        private String profilePictureUrl = "http://twitter.com/kdonald/a_new_picture";

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#test(java.lang.Object)
         */
        public boolean test(TestTwitterApi api) {
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#setConnectionValues(
         * java.lang.Object,
         * org.springframework.social.connect.ConnectionValues)
         */
        public void setConnectionValues(TestTwitterApi api, ConnectionValues values) {
            values.setProviderUserId(accountId);
            values.setDisplayName(name);
            values.setProfileUrl(profileUrl);
            values.setImageUrl(profilePictureUrl);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#fetchUserProfile(java.
         * lang.Object)
         */
        public UserProfile fetchUserProfile(TestTwitterApi api) {
            return new UserProfileBuilder().setName(name).setUsername("kdonald").build();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.social.connect.ApiAdapter#updateStatus(java.lang.
         * Object, java.lang.String)
         */
        public void updateStatus(TestTwitterApi api, String message) {
        }

    }
}
