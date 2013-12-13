/*
 * soapUI, copyright (C) 2004-2013 smartbear.com
 *
 * soapUI is free software; you can redistribute it and/or modify it under the
 * terms of version 2.1 of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 *
 * soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details at gnu.org.
 *
 */
package com.eviware.soapui.model;

import com.eviware.soapui.impl.rest.OAuth2Profile;
import com.eviware.soapui.impl.rest.OAuth2ProfileContainer;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.support.SoapUIException;
import org.apache.xmlbeans.XmlException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OAuthConfigTest
{
	private WsdlProject project;

	private OAuth2Profile oAuth2Profile;
	private OAuth2ProfileContainer oAuth2ProfileContainer;
	private String projectFileName = "OAuthTestProject.xml";

	@Before
	public void setUp() throws XmlException, IOException, SoapUIException
	{
		project = new WsdlProject();

		RestService restService = ( RestService )project.addNewInterface( "Test", RestServiceFactory.REST_TYPE );
		restService.addNewResource( "Resource", "/test" );

		oAuth2ProfileContainer = project.getOAuth2ProfileContainer();

		oAuth2Profile = oAuth2ProfileContainer.addNewOAuth2Profile();
		oAuth2Profile.setClientID( "google" );
		oAuth2Profile.setAccessTokenURI( "http://google.com/accessTokenURI" );
		oAuth2Profile.setAuthorizationURI( "http://google.com/auth" );
		oAuth2Profile.setClientSecret( "XYSDKMLL" );
		oAuth2Profile.setAccessToken( "ACDFECDSFKJFK#SDFSD8df" );
		oAuth2Profile.setScope( "google.com/calendar/read" );

	}

	@After
	public void tearDown()
	{
		File file = new File( projectFileName );
		file.delete();
	}

	@Test
	public void basicOAuthConfigIsProjectSpecific() throws Exception
	{

		project.saveAs( projectFileName );

		WsdlProject retrievedProject = new WsdlProject( projectFileName );

		assertThat( retrievedProject.getOAuth2ProfileContainer().getOAuth2ProfileList().size(), is( 1 ) );
		OAuth2Profile savedOAuth2Profile = retrievedProject.getOAuth2ProfileContainer().getOAuth2ProfileList().get( 0 );

		assertOAuth2ProfileFields( savedOAuth2Profile );

	}

	@Test
	public void basicOAuthConfigIsSaved() throws Exception
	{

		project.saveAs( projectFileName );

		WsdlProject retrievedProject = new WsdlProject( projectFileName );

		assertThat( retrievedProject.getOAuth2ProfileContainer().getOAuth2ProfileList().size(), is( 1 ) );
		OAuth2Profile savedOAuth2Profile = retrievedProject.getOAuth2ProfileContainer().getOAuth2ProfileList().get( 0 );

		assertOAuth2ProfileFields( savedOAuth2Profile );

	}

	private void assertOAuth2ProfileFields( OAuth2Profile savedOAuth2Profile )
	{
		assertThat( savedOAuth2Profile.getClientID(), is( oAuth2Profile.getClientID() ) );
		assertThat( savedOAuth2Profile.getAccessTokenURI(), is( oAuth2Profile.getAccessTokenURI() ) );
		assertThat( savedOAuth2Profile.getAuthorizationURI(), is( oAuth2Profile.getAuthorizationURI() ) );
		assertThat( savedOAuth2Profile.getClientSecret(), is( oAuth2Profile.getClientSecret() ) );
		assertThat( savedOAuth2Profile.getAccessToken(), is( oAuth2Profile.getAccessToken() ) );
		assertThat( savedOAuth2Profile.getScope(), is( oAuth2Profile.getScope() ) );
	}
}