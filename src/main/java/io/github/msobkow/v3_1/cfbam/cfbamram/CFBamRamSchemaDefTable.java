
// Description: Java 25 in-memory RAM DbIO implementation for SchemaDef.

/*
 *	io.github.msobkow.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFBam - Business Application Model
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later.
 *	
 *	Mark's Code Fractal CFBam is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFBam is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with Mark's Code Fractal CFBam.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 *	
 */

package io.github.msobkow.v3_1.cfbam.cfbamram;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;

import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfbam.cfbam.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;

/*
 *	CFBamRamSchemaDefTable in-memory RAM DbIO implementation
 *	for SchemaDef.
 */
public class CFBamRamSchemaDefTable
	implements ICFBamSchemaDefTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamSchemaDefBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamSchemaDefBuff >();
	private Map< CFBamSchemaDefByCTenantIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >> dictByCTenantIdx
		= new HashMap< CFBamSchemaDefByCTenantIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >>();
	private Map< CFBamSchemaDefByMinorVersionIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >> dictByMinorVersionIdx
		= new HashMap< CFBamSchemaDefByMinorVersionIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >>();
	private Map< CFBamSchemaDefByUNameIdxKey,
			CFBamSchemaDefBuff > dictByUNameIdx
		= new HashMap< CFBamSchemaDefByUNameIdxKey,
			CFBamSchemaDefBuff >();
	private Map< CFBamSchemaDefByAuthEMailIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >> dictByAuthEMailIdx
		= new HashMap< CFBamSchemaDefByAuthEMailIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >>();
	private Map< CFBamSchemaDefByProjectURLIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >> dictByProjectURLIdx
		= new HashMap< CFBamSchemaDefByProjectURLIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaDefBuff >>();
	private Map< CFBamSchemaDefByPubURIIdxKey,
			CFBamSchemaDefBuff > dictByPubURIIdx
		= new HashMap< CFBamSchemaDefByPubURIIdxKey,
			CFBamSchemaDefBuff >();

	public CFBamRamSchemaDefTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createSchemaDef( CFSecAuthorization Authorization,
		CFBamSchemaDefBuff Buff )
	{
		final String S_ProcName = "createSchemaDef";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaDefByCTenantIdxKey keyCTenantIdx = schema.getFactorySchemaDef().newCTenantIdxKey();
		keyCTenantIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );

		CFBamSchemaDefByMinorVersionIdxKey keyMinorVersionIdx = schema.getFactorySchemaDef().newMinorVersionIdxKey();
		keyMinorVersionIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );

		CFBamSchemaDefByUNameIdxKey keyUNameIdx = schema.getFactorySchemaDef().newUNameIdxKey();
		keyUNameIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamSchemaDefByAuthEMailIdxKey keyAuthEMailIdx = schema.getFactorySchemaDef().newAuthEMailIdxKey();
		keyAuthEMailIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		keyAuthEMailIdx.setRequiredAuthorEMail( Buff.getRequiredAuthorEMail() );

		CFBamSchemaDefByProjectURLIdxKey keyProjectURLIdx = schema.getFactorySchemaDef().newProjectURLIdxKey();
		keyProjectURLIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		keyProjectURLIdx.setRequiredProjectURL( Buff.getRequiredProjectURL() );

		CFBamSchemaDefByPubURIIdxKey keyPubURIIdx = schema.getFactorySchemaDef().newPubURIIdxKey();
		keyPubURIIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		keyPubURIIdx.setRequiredPublishURI( Buff.getRequiredPublishURI() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"SchemaDefUNameIdx",
				keyUNameIdx );
		}

		if( dictByPubURIIdx.containsKey( keyPubURIIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"SchemaPublishURIIdx",
				keyPubURIIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableMinorVersion().readDerivedByIdIdx( Authorization,
						Buff.getRequiredMinorVersionId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"MinorVersion",
						"MinorVersion",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredCTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Owner",
						"CTenant",
						"Tenant",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictCTenantIdx;
		if( dictByCTenantIdx.containsKey( keyCTenantIdx ) ) {
			subdictCTenantIdx = dictByCTenantIdx.get( keyCTenantIdx );
		}
		else {
			subdictCTenantIdx = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByCTenantIdx.put( keyCTenantIdx, subdictCTenantIdx );
		}
		subdictCTenantIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictMinorVersionIdx;
		if( dictByMinorVersionIdx.containsKey( keyMinorVersionIdx ) ) {
			subdictMinorVersionIdx = dictByMinorVersionIdx.get( keyMinorVersionIdx );
		}
		else {
			subdictMinorVersionIdx = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByMinorVersionIdx.put( keyMinorVersionIdx, subdictMinorVersionIdx );
		}
		subdictMinorVersionIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictAuthEMailIdx;
		if( dictByAuthEMailIdx.containsKey( keyAuthEMailIdx ) ) {
			subdictAuthEMailIdx = dictByAuthEMailIdx.get( keyAuthEMailIdx );
		}
		else {
			subdictAuthEMailIdx = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByAuthEMailIdx.put( keyAuthEMailIdx, subdictAuthEMailIdx );
		}
		subdictAuthEMailIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictProjectURLIdx;
		if( dictByProjectURLIdx.containsKey( keyProjectURLIdx ) ) {
			subdictProjectURLIdx = dictByProjectURLIdx.get( keyProjectURLIdx );
		}
		else {
			subdictProjectURLIdx = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByProjectURLIdx.put( keyProjectURLIdx, subdictProjectURLIdx );
		}
		subdictProjectURLIdx.put( pkey, Buff );

		dictByPubURIIdx.put( keyPubURIIdx, Buff );

	}

	public CFBamSchemaDefBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaDefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaDefBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaDefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaDefBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaDef.readAllDerived";
		CFBamSchemaDefBuff[] retList = new CFBamSchemaDefBuff[ dictByPKey.values().size() ];
		Iterator< CFBamSchemaDefBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamSchemaDefBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamScopeBuff buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamScopeBuff buff;
			ArrayList<CFBamSchemaDefBuff> filteredList = new ArrayList<CFBamSchemaDefBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaDefBuff ) ) {
					filteredList.add( (CFBamSchemaDefBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaDefBuff[0] ) );
		}
	}

	public CFBamSchemaDefBuff[] readDerivedByCTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByCTenantIdx";
		CFBamSchemaDefByCTenantIdxKey key = schema.getFactorySchemaDef().newCTenantIdxKey();
		key.setRequiredCTenantId( CTenantId );

		CFBamSchemaDefBuff[] recArray;
		if( dictByCTenantIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictCTenantIdx
				= dictByCTenantIdx.get( key );
			recArray = new CFBamSchemaDefBuff[ subdictCTenantIdx.size() ];
			Iterator< CFBamSchemaDefBuff > iter = subdictCTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictCTenantIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByCTenantIdx.put( key, subdictCTenantIdx );
			recArray = new CFBamSchemaDefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaDefBuff[] readDerivedByMinorVersionIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByMinorVersionIdx";
		CFBamSchemaDefByMinorVersionIdxKey key = schema.getFactorySchemaDef().newMinorVersionIdxKey();
		key.setRequiredMinorVersionId( MinorVersionId );

		CFBamSchemaDefBuff[] recArray;
		if( dictByMinorVersionIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictMinorVersionIdx
				= dictByMinorVersionIdx.get( key );
			recArray = new CFBamSchemaDefBuff[ subdictMinorVersionIdx.size() ];
			Iterator< CFBamSchemaDefBuff > iter = subdictMinorVersionIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictMinorVersionIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByMinorVersionIdx.put( key, subdictMinorVersionIdx );
			recArray = new CFBamSchemaDefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaDefBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByUNameIdx";
		CFBamSchemaDefByUNameIdxKey key = schema.getFactorySchemaDef().newUNameIdxKey();
		key.setRequiredMinorVersionId( MinorVersionId );
		key.setRequiredName( Name );

		CFBamSchemaDefBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaDefBuff[] readDerivedByAuthEMailIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String AuthorEMail )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByAuthEMailIdx";
		CFBamSchemaDefByAuthEMailIdxKey key = schema.getFactorySchemaDef().newAuthEMailIdxKey();
		key.setRequiredCTenantId( CTenantId );
		key.setRequiredAuthorEMail( AuthorEMail );

		CFBamSchemaDefBuff[] recArray;
		if( dictByAuthEMailIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictAuthEMailIdx
				= dictByAuthEMailIdx.get( key );
			recArray = new CFBamSchemaDefBuff[ subdictAuthEMailIdx.size() ];
			Iterator< CFBamSchemaDefBuff > iter = subdictAuthEMailIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictAuthEMailIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByAuthEMailIdx.put( key, subdictAuthEMailIdx );
			recArray = new CFBamSchemaDefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaDefBuff[] readDerivedByProjectURLIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String ProjectURL )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByProjectURLIdx";
		CFBamSchemaDefByProjectURLIdxKey key = schema.getFactorySchemaDef().newProjectURLIdxKey();
		key.setRequiredCTenantId( CTenantId );
		key.setRequiredProjectURL( ProjectURL );

		CFBamSchemaDefBuff[] recArray;
		if( dictByProjectURLIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictProjectURLIdx
				= dictByProjectURLIdx.get( key );
			recArray = new CFBamSchemaDefBuff[ subdictProjectURLIdx.size() ];
			Iterator< CFBamSchemaDefBuff > iter = subdictProjectURLIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaDefBuff > subdictProjectURLIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByProjectURLIdx.put( key, subdictProjectURLIdx );
			recArray = new CFBamSchemaDefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaDefBuff readDerivedByPubURIIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String PublishURI )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByPubURIIdx";
		CFBamSchemaDefByPubURIIdxKey key = schema.getFactorySchemaDef().newPubURIIdxKey();
		key.setRequiredCTenantId( CTenantId );
		key.setRequiredPublishURI( PublishURI );

		CFBamSchemaDefBuff buff;
		if( dictByPubURIIdx.containsKey( key ) ) {
			buff = dictByPubURIIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaDefBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamSchemaDefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaDefBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readBuff";
		CFBamSchemaDefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a802" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaDefBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamSchemaDefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a802" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaDefBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readAllBuff";
		CFBamSchemaDefBuff buff;
		ArrayList<CFBamSchemaDefBuff> filteredList = new ArrayList<CFBamSchemaDefBuff>();
		CFBamSchemaDefBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a802" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaDefBuff[0] ) );
	}

	public CFBamSchemaDefBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamSchemaDefBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamSchemaDefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaDefBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamSchemaDefBuff buff;
		ArrayList<CFBamSchemaDefBuff> filteredList = new ArrayList<CFBamSchemaDefBuff>();
		CFBamSchemaDefBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamSchemaDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaDefBuff[0] ) );
	}

	public CFBamSchemaDefBuff[] readBuffByCTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readBuffByCTenantIdx() ";
		CFBamSchemaDefBuff buff;
		ArrayList<CFBamSchemaDefBuff> filteredList = new ArrayList<CFBamSchemaDefBuff>();
		CFBamSchemaDefBuff[] buffList = readDerivedByCTenantIdx( Authorization,
			CTenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a802" ) ) {
				filteredList.add( (CFBamSchemaDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaDefBuff[0] ) );
	}

	public CFBamSchemaDefBuff[] readBuffByMinorVersionIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readBuffByMinorVersionIdx() ";
		CFBamSchemaDefBuff buff;
		ArrayList<CFBamSchemaDefBuff> filteredList = new ArrayList<CFBamSchemaDefBuff>();
		CFBamSchemaDefBuff[] buffList = readDerivedByMinorVersionIdx( Authorization,
			MinorVersionId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a802" ) ) {
				filteredList.add( (CFBamSchemaDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaDefBuff[0] ) );
	}

	public CFBamSchemaDefBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readBuffByUNameIdx() ";
		CFBamSchemaDefBuff buff = readDerivedByUNameIdx( Authorization,
			MinorVersionId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a802" ) ) {
			return( (CFBamSchemaDefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaDefBuff[] readBuffByAuthEMailIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String AuthorEMail )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readBuffByAuthEMailIdx() ";
		CFBamSchemaDefBuff buff;
		ArrayList<CFBamSchemaDefBuff> filteredList = new ArrayList<CFBamSchemaDefBuff>();
		CFBamSchemaDefBuff[] buffList = readDerivedByAuthEMailIdx( Authorization,
			CTenantId,
			AuthorEMail );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a802" ) ) {
				filteredList.add( (CFBamSchemaDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaDefBuff[0] ) );
	}

	public CFBamSchemaDefBuff[] readBuffByProjectURLIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String ProjectURL )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readBuffByProjectURLIdx() ";
		CFBamSchemaDefBuff buff;
		ArrayList<CFBamSchemaDefBuff> filteredList = new ArrayList<CFBamSchemaDefBuff>();
		CFBamSchemaDefBuff[] buffList = readDerivedByProjectURLIdx( Authorization,
			CTenantId,
			ProjectURL );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a802" ) ) {
				filteredList.add( (CFBamSchemaDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaDefBuff[0] ) );
	}

	public CFBamSchemaDefBuff readBuffByPubURIIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String PublishURI )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readBuffByPubURIIdx() ";
		CFBamSchemaDefBuff buff = readDerivedByPubURIIdx( Authorization,
			CTenantId,
			PublishURI );
		if( ( buff != null ) && buff.getClassCode().equals( "a802" ) ) {
			return( (CFBamSchemaDefBuff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific SchemaDef buffer instances identified by the duplicate key CTenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	CTenantId	The SchemaDef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaDefBuff[] pageBuffByCTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByCTenantIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaDef buffer instances identified by the duplicate key MinorVersionIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	MinorVersionId	The SchemaDef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaDefBuff[] pageBuffByMinorVersionIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByMinorVersionIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaDef buffer instances identified by the duplicate key AuthEMailIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	CTenantId	The SchemaDef key attribute of the instance generating the id.
	 *
	 *	@param	AuthorEMail	The SchemaDef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaDefBuff[] pageBuffByAuthEMailIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String AuthorEMail,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByAuthEMailIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaDef buffer instances identified by the duplicate key ProjectURLIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	CTenantId	The SchemaDef key attribute of the instance generating the id.
	 *
	 *	@param	ProjectURL	The SchemaDef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaDefBuff[] pageBuffByProjectURLIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String ProjectURL,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByProjectURLIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateSchemaDef( CFSecAuthorization Authorization,
		CFBamSchemaDefBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaDefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaDef",
				"Existing record not found",
				"SchemaDef",
				pkey );
		}
		CFBamSchemaDefByCTenantIdxKey existingKeyCTenantIdx = schema.getFactorySchemaDef().newCTenantIdxKey();
		existingKeyCTenantIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );

		CFBamSchemaDefByCTenantIdxKey newKeyCTenantIdx = schema.getFactorySchemaDef().newCTenantIdxKey();
		newKeyCTenantIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );

		CFBamSchemaDefByMinorVersionIdxKey existingKeyMinorVersionIdx = schema.getFactorySchemaDef().newMinorVersionIdxKey();
		existingKeyMinorVersionIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );

		CFBamSchemaDefByMinorVersionIdxKey newKeyMinorVersionIdx = schema.getFactorySchemaDef().newMinorVersionIdxKey();
		newKeyMinorVersionIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );

		CFBamSchemaDefByUNameIdxKey existingKeyUNameIdx = schema.getFactorySchemaDef().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamSchemaDefByUNameIdxKey newKeyUNameIdx = schema.getFactorySchemaDef().newUNameIdxKey();
		newKeyUNameIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamSchemaDefByAuthEMailIdxKey existingKeyAuthEMailIdx = schema.getFactorySchemaDef().newAuthEMailIdxKey();
		existingKeyAuthEMailIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		existingKeyAuthEMailIdx.setRequiredAuthorEMail( existing.getRequiredAuthorEMail() );

		CFBamSchemaDefByAuthEMailIdxKey newKeyAuthEMailIdx = schema.getFactorySchemaDef().newAuthEMailIdxKey();
		newKeyAuthEMailIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		newKeyAuthEMailIdx.setRequiredAuthorEMail( Buff.getRequiredAuthorEMail() );

		CFBamSchemaDefByProjectURLIdxKey existingKeyProjectURLIdx = schema.getFactorySchemaDef().newProjectURLIdxKey();
		existingKeyProjectURLIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		existingKeyProjectURLIdx.setRequiredProjectURL( existing.getRequiredProjectURL() );

		CFBamSchemaDefByProjectURLIdxKey newKeyProjectURLIdx = schema.getFactorySchemaDef().newProjectURLIdxKey();
		newKeyProjectURLIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		newKeyProjectURLIdx.setRequiredProjectURL( Buff.getRequiredProjectURL() );

		CFBamSchemaDefByPubURIIdxKey existingKeyPubURIIdx = schema.getFactorySchemaDef().newPubURIIdxKey();
		existingKeyPubURIIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		existingKeyPubURIIdx.setRequiredPublishURI( existing.getRequiredPublishURI() );

		CFBamSchemaDefByPubURIIdxKey newKeyPubURIIdx = schema.getFactorySchemaDef().newPubURIIdxKey();
		newKeyPubURIIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		newKeyPubURIIdx.setRequiredPublishURI( Buff.getRequiredPublishURI() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateSchemaDef",
					"SchemaDefUNameIdx",
					newKeyUNameIdx );
			}
		}

		if( ! existingKeyPubURIIdx.equals( newKeyPubURIIdx ) ) {
			if( dictByPubURIIdx.containsKey( newKeyPubURIIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateSchemaDef",
					"SchemaPublishURIIdx",
					newKeyPubURIIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaDef",
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableMinorVersion().readDerivedByIdIdx( Authorization,
						Buff.getRequiredMinorVersionId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaDef",
						"Container",
						"MinorVersion",
						"MinorVersion",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredCTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaDef",
						"Owner",
						"CTenant",
						"Tenant",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamSchemaDefBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByCTenantIdx.get( existingKeyCTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByCTenantIdx.containsKey( newKeyCTenantIdx ) ) {
			subdict = dictByCTenantIdx.get( newKeyCTenantIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByCTenantIdx.put( newKeyCTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByMinorVersionIdx.get( existingKeyMinorVersionIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByMinorVersionIdx.containsKey( newKeyMinorVersionIdx ) ) {
			subdict = dictByMinorVersionIdx.get( newKeyMinorVersionIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByMinorVersionIdx.put( newKeyMinorVersionIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByAuthEMailIdx.get( existingKeyAuthEMailIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByAuthEMailIdx.containsKey( newKeyAuthEMailIdx ) ) {
			subdict = dictByAuthEMailIdx.get( newKeyAuthEMailIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByAuthEMailIdx.put( newKeyAuthEMailIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByProjectURLIdx.get( existingKeyProjectURLIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByProjectURLIdx.containsKey( newKeyProjectURLIdx ) ) {
			subdict = dictByProjectURLIdx.get( newKeyProjectURLIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaDefBuff >();
			dictByProjectURLIdx.put( newKeyProjectURLIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByPubURIIdx.remove( existingKeyPubURIIdx );
		dictByPubURIIdx.put( newKeyPubURIIdx, Buff );

	}

	public void deleteSchemaDef( CFSecAuthorization Authorization,
		CFBamSchemaDefBuff Buff )
	{
		final String S_ProcName = "CFBamRamSchemaDefTable.deleteSchemaDef() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaDefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaDef",
				pkey );
		}
			CFBamValueBuff buffClearTypeReferences;
			CFBamValueBuff arrClearTypeReferences[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			existing.getRequiredId() );
			for( int idxClearTypeReferences = 0; idxClearTypeReferences < arrClearTypeReferences.length; idxClearTypeReferences++ ) {
				buffClearTypeReferences = arrClearTypeReferences[idxClearTypeReferences];
				CFBamTableColBuff buffReferencingTableCols;
				CFBamTableColBuff arrReferencingTableCols[] = schema.getTableTableCol().readDerivedByDataIdx( Authorization,
				buffClearTypeReferences.getRequiredId() );
				for( int idxReferencingTableCols = 0; idxReferencingTableCols < arrReferencingTableCols.length; idxReferencingTableCols++ ) {
					buffReferencingTableCols = arrReferencingTableCols[idxReferencingTableCols];
					{
						CFBamTableColBuff editBuff = schema.getTableTableCol().readDerivedByIdIdx( Authorization,
							buffReferencingTableCols.getRequiredId() );
						editBuff.setOptionalDataId( null );
						classCode = editBuff.getClassCode();
						if( classCode.equals( "a858" ) ) {
							schema.getTableTableCol().updateTableCol( Authorization, editBuff );
						}
						else {
							new CFLibUnsupportedClassException( getClass(),
								S_ProcName,
								"Unrecognized ClassCode \"" + classCode + "\"" );
						}
					}
				}
			}
			CFBamTableBuff buffClearTableRelationNarrowed;
			CFBamTableBuff arrClearTableRelationNarrowed[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
			for( int idxClearTableRelationNarrowed = 0; idxClearTableRelationNarrowed < arrClearTableRelationNarrowed.length; idxClearTableRelationNarrowed++ ) {
				buffClearTableRelationNarrowed = arrClearTableRelationNarrowed[idxClearTableRelationNarrowed];
				CFBamRelationBuff buffTableRelation;
				CFBamRelationBuff arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
				buffClearTableRelationNarrowed.getRequiredId() );
				for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
					buffTableRelation = arrTableRelation[idxTableRelation];
					{
						CFBamRelationBuff editBuff = schema.getTableRelation().readDerivedByIdIdx( Authorization,
							buffTableRelation.getRequiredId() );
						editBuff.setOptionalNarrowedId( null );
						classCode = editBuff.getClassCode();
						if( classCode.equals( "a835" ) ) {
							schema.getTableRelation().updateRelation( Authorization, editBuff );
						}
						else {
							new CFLibUnsupportedClassException( getClass(),
								S_ProcName,
								"Unrecognized ClassCode \"" + classCode + "\"" );
						}
					}
				}
			}
		CFBamTableBuff buffDelTableMethods;
		CFBamTableBuff arrDelTableMethods[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableMethods = 0; idxDelTableMethods < arrDelTableMethods.length; idxDelTableMethods++ ) {
			buffDelTableMethods = arrDelTableMethods[idxDelTableMethods];
					schema.getTableServerMethod().deleteServerMethodByMethTableIdx( Authorization,
						buffDelTableMethods.getRequiredId() );
		}
		CFBamTableBuff buffDelTableDelDep;
		CFBamTableBuff arrDelTableDelDep[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableDelDep = 0; idxDelTableDelDep < arrDelTableDelDep.length; idxDelTableDelDep++ ) {
			buffDelTableDelDep = arrDelTableDelDep[idxDelTableDelDep];
					schema.getTableDelTopDep().deleteDelTopDepByDelTopDepTblIdx( Authorization,
						buffDelTableDelDep.getRequiredId() );
		}
		CFBamTableBuff buffDelTableClearDep;
		CFBamTableBuff arrDelTableClearDep[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableClearDep = 0; idxDelTableClearDep < arrDelTableClearDep.length; idxDelTableClearDep++ ) {
			buffDelTableClearDep = arrDelTableClearDep[idxDelTableClearDep];
					schema.getTableClearTopDep().deleteClearTopDepByClrTopDepTblIdx( Authorization,
						buffDelTableClearDep.getRequiredId() );
		}
		CFBamTableBuff buffDelTableChain;
		CFBamTableBuff arrDelTableChain[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableChain = 0; idxDelTableChain < arrDelTableChain.length; idxDelTableChain++ ) {
			buffDelTableChain = arrDelTableChain[idxDelTableChain];
					schema.getTableChain().deleteChainByChainTableIdx( Authorization,
						buffDelTableChain.getRequiredId() );
		}
		CFBamTableBuff buffDelTableRelationPopDep;
		CFBamTableBuff arrDelTableRelationPopDep[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationPopDep = 0; idxDelTableRelationPopDep < arrDelTableRelationPopDep.length; idxDelTableRelationPopDep++ ) {
			buffDelTableRelationPopDep = arrDelTableRelationPopDep[idxDelTableRelationPopDep];
			CFBamRelationBuff buffTableRelation;
			CFBamRelationBuff arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
				buffDelTableRelationPopDep.getRequiredId() );
			for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
				buffTableRelation = arrTableRelation[idxTableRelation];
					schema.getTablePopTopDep().deletePopTopDepByContRelIdx( Authorization,
						buffTableRelation.getRequiredId() );
			}
		}
		CFBamTableBuff buffDelTableRelationCol;
		CFBamTableBuff arrDelTableRelationCol[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationCol = 0; idxDelTableRelationCol < arrDelTableRelationCol.length; idxDelTableRelationCol++ ) {
			buffDelTableRelationCol = arrDelTableRelationCol[idxDelTableRelationCol];
			CFBamRelationBuff buffTableRelation;
			CFBamRelationBuff arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
				buffDelTableRelationCol.getRequiredId() );
			for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
				buffTableRelation = arrTableRelation[idxTableRelation];
					schema.getTableRelationCol().deleteRelationColByRelationIdx( Authorization,
						buffTableRelation.getRequiredId() );
			}
		}
		CFBamTableBuff buffDelTableRelation;
		CFBamTableBuff arrDelTableRelation[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelation = 0; idxDelTableRelation < arrDelTableRelation.length; idxDelTableRelation++ ) {
			buffDelTableRelation = arrDelTableRelation[idxDelTableRelation];
					schema.getTableRelation().deleteRelationByRelTableIdx( Authorization,
						buffDelTableRelation.getRequiredId() );
		}
		CFBamTableBuff buffDelTableIndexRefRelFmCol;
		CFBamTableBuff arrDelTableIndexRefRelFmCol[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelFmCol = 0; idxDelTableIndexRefRelFmCol < arrDelTableIndexRefRelFmCol.length; idxDelTableIndexRefRelFmCol++ ) {
			buffDelTableIndexRefRelFmCol = arrDelTableIndexRefRelFmCol[idxDelTableIndexRefRelFmCol];
			CFBamIndexBuff buffTableIndex;
			CFBamIndexBuff arrTableIndex[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
				buffDelTableIndexRefRelFmCol.getRequiredId() );
			for( int idxTableIndex = 0; idxTableIndex < arrTableIndex.length; idxTableIndex++ ) {
				buffTableIndex = arrTableIndex[idxTableIndex];
			CFBamIndexColBuff buffColumns;
			CFBamIndexColBuff arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
					buffTableIndex.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = arrColumns[idxColumns];
					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
			}
		}
		CFBamTableBuff buffDelTableIndexRefRelToCol;
		CFBamTableBuff arrDelTableIndexRefRelToCol[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelToCol = 0; idxDelTableIndexRefRelToCol < arrDelTableIndexRefRelToCol.length; idxDelTableIndexRefRelToCol++ ) {
			buffDelTableIndexRefRelToCol = arrDelTableIndexRefRelToCol[idxDelTableIndexRefRelToCol];
			CFBamIndexBuff buffTableIndex;
			CFBamIndexBuff arrTableIndex[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
				buffDelTableIndexRefRelToCol.getRequiredId() );
			for( int idxTableIndex = 0; idxTableIndex < arrTableIndex.length; idxTableIndex++ ) {
				buffTableIndex = arrTableIndex[idxTableIndex];
			CFBamIndexColBuff buffColumns;
			CFBamIndexColBuff arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
					buffTableIndex.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = arrColumns[idxColumns];
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
			}
		}
		CFBamTableBuff buffDelTableIndexCols;
		CFBamTableBuff arrDelTableIndexCols[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexCols = 0; idxDelTableIndexCols < arrDelTableIndexCols.length; idxDelTableIndexCols++ ) {
			buffDelTableIndexCols = arrDelTableIndexCols[idxDelTableIndexCols];
			CFBamIndexBuff buffTableIndex;
			CFBamIndexBuff arrTableIndex[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
				buffDelTableIndexCols.getRequiredId() );
			for( int idxTableIndex = 0; idxTableIndex < arrTableIndex.length; idxTableIndex++ ) {
				buffTableIndex = arrTableIndex[idxTableIndex];
					schema.getTableIndexCol().deleteIndexColByIndexIdx( Authorization,
						buffTableIndex.getRequiredId() );
			}
		}
		CFBamTableBuff buffDelTableIndexes;
		CFBamTableBuff arrDelTableIndexes[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexes = 0; idxDelTableIndexes < arrDelTableIndexes.length; idxDelTableIndexes++ ) {
			buffDelTableIndexes = arrDelTableIndexes[idxDelTableIndexes];
					schema.getTableIndex().deleteIndexByIdxTableIdx( Authorization,
						buffDelTableIndexes.getRequiredId() );
		}
		CFBamTableBuff buffDelTableRefIndexColumns;
		CFBamTableBuff arrDelTableRefIndexColumns[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRefIndexColumns = 0; idxDelTableRefIndexColumns < arrDelTableRefIndexColumns.length; idxDelTableRefIndexColumns++ ) {
			buffDelTableRefIndexColumns = arrDelTableRefIndexColumns[idxDelTableRefIndexColumns];
			CFBamValueBuff buffColumns;
			CFBamValueBuff arrColumns[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
				buffDelTableRefIndexColumns.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = arrColumns[idxColumns];
					schema.getTableIndexCol().deleteIndexColByColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
		}
		CFBamTableBuff buffDelTableColumns;
		CFBamTableBuff arrDelTableColumns[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableColumns = 0; idxDelTableColumns < arrDelTableColumns.length; idxDelTableColumns++ ) {
			buffDelTableColumns = arrDelTableColumns[idxDelTableColumns];
					schema.getTableValue().deleteValueByScopeIdx( Authorization,
						buffDelTableColumns.getRequiredId() );
		}
					schema.getTableTable().deleteTableBySchemaDefIdx( Authorization,
						existing.getRequiredId() );
		CFBamValueBuff buffDelTypeRefs;
		CFBamValueBuff arrDelTypeRefs[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTypeRefs = 0; idxDelTypeRefs < arrDelTypeRefs.length; idxDelTypeRefs++ ) {
			buffDelTypeRefs = arrDelTypeRefs[idxDelTypeRefs];
					schema.getTableTableCol().deleteTableColByDataIdx( Authorization,
						buffDelTypeRefs.getRequiredId() );
		}
					schema.getTableValue().deleteValueByScopeIdx( Authorization,
						existing.getRequiredId() );
		CFBamSchemaDefByCTenantIdxKey keyCTenantIdx = schema.getFactorySchemaDef().newCTenantIdxKey();
		keyCTenantIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );

		CFBamSchemaDefByMinorVersionIdxKey keyMinorVersionIdx = schema.getFactorySchemaDef().newMinorVersionIdxKey();
		keyMinorVersionIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );

		CFBamSchemaDefByUNameIdxKey keyUNameIdx = schema.getFactorySchemaDef().newUNameIdxKey();
		keyUNameIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamSchemaDefByAuthEMailIdxKey keyAuthEMailIdx = schema.getFactorySchemaDef().newAuthEMailIdxKey();
		keyAuthEMailIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		keyAuthEMailIdx.setRequiredAuthorEMail( existing.getRequiredAuthorEMail() );

		CFBamSchemaDefByProjectURLIdxKey keyProjectURLIdx = schema.getFactorySchemaDef().newProjectURLIdxKey();
		keyProjectURLIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		keyProjectURLIdx.setRequiredProjectURL( existing.getRequiredProjectURL() );

		CFBamSchemaDefByPubURIIdxKey keyPubURIIdx = schema.getFactorySchemaDef().newPubURIIdxKey();
		keyPubURIIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		keyPubURIIdx.setRequiredPublishURI( existing.getRequiredPublishURI() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamSchemaDefBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByCTenantIdx.get( keyCTenantIdx );
		subdict.remove( pkey );

		subdict = dictByMinorVersionIdx.get( keyMinorVersionIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByAuthEMailIdx.get( keyAuthEMailIdx );
		subdict.remove( pkey );

		subdict = dictByProjectURLIdx.get( keyProjectURLIdx );
		subdict.remove( pkey );

		dictByPubURIIdx.remove( keyPubURIIdx );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteSchemaDefByCTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId )
	{
		CFBamSchemaDefByCTenantIdxKey key = schema.getFactorySchemaDef().newCTenantIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		deleteSchemaDefByCTenantIdx( Authorization, key );
	}

	public void deleteSchemaDefByCTenantIdx( CFSecAuthorization Authorization,
		CFBamSchemaDefByCTenantIdxKey argKey )
	{
		CFBamSchemaDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}

	public void deleteSchemaDefByMinorVersionIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argMinorVersionId )
	{
		CFBamSchemaDefByMinorVersionIdxKey key = schema.getFactorySchemaDef().newMinorVersionIdxKey();
		key.setRequiredMinorVersionId( argMinorVersionId );
		deleteSchemaDefByMinorVersionIdx( Authorization, key );
	}

	public void deleteSchemaDefByMinorVersionIdx( CFSecAuthorization Authorization,
		CFBamSchemaDefByMinorVersionIdxKey argKey )
	{
		CFBamSchemaDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}

	public void deleteSchemaDefByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argMinorVersionId,
		String argName )
	{
		CFBamSchemaDefByUNameIdxKey key = schema.getFactorySchemaDef().newUNameIdxKey();
		key.setRequiredMinorVersionId( argMinorVersionId );
		key.setRequiredName( argName );
		deleteSchemaDefByUNameIdx( Authorization, key );
	}

	public void deleteSchemaDefByUNameIdx( CFSecAuthorization Authorization,
		CFBamSchemaDefByUNameIdxKey argKey )
	{
		CFBamSchemaDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}

	public void deleteSchemaDefByAuthEMailIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId,
		String argAuthorEMail )
	{
		CFBamSchemaDefByAuthEMailIdxKey key = schema.getFactorySchemaDef().newAuthEMailIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		key.setRequiredAuthorEMail( argAuthorEMail );
		deleteSchemaDefByAuthEMailIdx( Authorization, key );
	}

	public void deleteSchemaDefByAuthEMailIdx( CFSecAuthorization Authorization,
		CFBamSchemaDefByAuthEMailIdxKey argKey )
	{
		CFBamSchemaDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}

	public void deleteSchemaDefByProjectURLIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId,
		String argProjectURL )
	{
		CFBamSchemaDefByProjectURLIdxKey key = schema.getFactorySchemaDef().newProjectURLIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		key.setRequiredProjectURL( argProjectURL );
		deleteSchemaDefByProjectURLIdx( Authorization, key );
	}

	public void deleteSchemaDefByProjectURLIdx( CFSecAuthorization Authorization,
		CFBamSchemaDefByProjectURLIdxKey argKey )
	{
		CFBamSchemaDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}

	public void deleteSchemaDefByPubURIIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId,
		String argPublishURI )
	{
		CFBamSchemaDefByPubURIIdxKey key = schema.getFactorySchemaDef().newPubURIIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		key.setRequiredPublishURI( argPublishURI );
		deleteSchemaDefByPubURIIdx( Authorization, key );
	}

	public void deleteSchemaDefByPubURIIdx( CFSecAuthorization Authorization,
		CFBamSchemaDefByPubURIIdxKey argKey )
	{
		CFBamSchemaDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}

	public void deleteSchemaDefByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteSchemaDefByIdIdx( Authorization, key );
	}

	public void deleteSchemaDefByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamSchemaDefBuff cur;
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}

	public void deleteSchemaDefByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaDefByTenantIdx( Authorization, key );
	}

	public void deleteSchemaDefByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamSchemaDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaDefBuff> matchSet = new LinkedList<CFBamSchemaDefBuff>();
		Iterator<CFBamSchemaDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaDef( Authorization, cur );
		}
	}
}
