
// Description: Java 25 in-memory RAM DbIO implementation for Chain.

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
import java.time.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;

import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfbam.cfbam.*;
import io.github.msobkow.v3_1.cfsec.cfsec.buff.*;
import io.github.msobkow.v3_1.cfint.cfint.buff.*;
import io.github.msobkow.v3_1.cfbam.cfbam.buff.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;

/*
 *	CFBamRamChainTable in-memory RAM DbIO implementation
 *	for Chain.
 */
public class CFBamRamChainTable
	implements ICFBamChainTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffChain > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffChain >();
	private Map< CFBamBuffChainByChainTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByChainTableIdx
		= new HashMap< CFBamBuffChainByChainTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();
	private Map< CFBamBuffChainByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffChainByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();
	private Map< CFBamBuffChainByUNameIdxKey,
			CFBamBuffChain > dictByUNameIdx
		= new HashMap< CFBamBuffChainByUNameIdxKey,
			CFBamBuffChain >();
	private Map< CFBamBuffChainByPrevRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByPrevRelIdx
		= new HashMap< CFBamBuffChainByPrevRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();
	private Map< CFBamBuffChainByNextRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByNextRelIdx
		= new HashMap< CFBamBuffChainByNextRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();

	public CFBamRamChainTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createChain( ICFSecAuthorization Authorization,
		ICFBamChain Buff )
	{
		final String S_ProcName = "createChain";
		CFLibDbKeyHash256 pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredId( schema.nextChainIdGen() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamBuffChainByChainTableIdxKey keyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		keyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffChainByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffChainByUNameIdxKey keyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffChainByPrevRelIdxKey keyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamBuffChainByNextRelIdxKey keyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		keyNextRelIdx.setRequiredNextRelationId( Buff.getRequiredNextRelationId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ChainUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPrevRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"PrevRelation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredNextRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"NextRelation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictChainTableIdx;
		if( dictByChainTableIdx.containsKey( keyChainTableIdx ) ) {
			subdictChainTableIdx = dictByChainTableIdx.get( keyChainTableIdx );
		}
		else {
			subdictChainTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByChainTableIdx.put( keyChainTableIdx, subdictChainTableIdx );
		}
		subdictChainTableIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictPrevRelIdx;
		if( dictByPrevRelIdx.containsKey( keyPrevRelIdx ) ) {
			subdictPrevRelIdx = dictByPrevRelIdx.get( keyPrevRelIdx );
		}
		else {
			subdictPrevRelIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByPrevRelIdx.put( keyPrevRelIdx, subdictPrevRelIdx );
		}
		subdictPrevRelIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictNextRelIdx;
		if( dictByNextRelIdx.containsKey( keyNextRelIdx ) ) {
			subdictNextRelIdx = dictByNextRelIdx.get( keyNextRelIdx );
		}
		else {
			subdictNextRelIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByNextRelIdx.put( keyNextRelIdx, subdictNextRelIdx );
		}
		subdictNextRelIdx.put( pkey, Buff );

	}

	public ICFBamChain readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamChain.readDerived";
		ICFBamChain buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamChain lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamChain.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryChain().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamChain buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamChain[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamChain.readAllDerived";
		ICFBamChain[] retList = new ICFBamChain[ dictByPKey.values().size() ];
		Iterator< ICFBamChain > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamChain[] readDerivedByChainTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByChainTableIdx";
		CFBamBuffChainByChainTableIdxKey key = schema.getFactoryChain().newChainTableIdxKey();
		key.setRequiredTableId( TableId );

		ICFBamChain[] recArray;
		if( dictByChainTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictChainTableIdx
				= dictByChainTableIdx.get( key );
			recArray = new ICFBamChain[ subdictChainTableIdx.size() ];
			Iterator< ICFBamChain > iter = subdictChainTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictChainTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByChainTableIdx.put( key, subdictChainTableIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	public ICFBamChain[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByDefSchemaIdx";
		CFBamBuffChainByDefSchemaIdxKey key = schema.getFactoryChain().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamChain[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamChain[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamChain > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	public ICFBamChain readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByUNameIdx";
		CFBamBuffChainByUNameIdxKey key = schema.getFactoryChain().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		ICFBamChain buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamChain[] readDerivedByPrevRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByPrevRelIdx";
		CFBamBuffChainByPrevRelIdxKey key = schema.getFactoryChain().newPrevRelIdxKey();
		key.setRequiredPrevRelationId( PrevRelationId );

		ICFBamChain[] recArray;
		if( dictByPrevRelIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictPrevRelIdx
				= dictByPrevRelIdx.get( key );
			recArray = new ICFBamChain[ subdictPrevRelIdx.size() ];
			Iterator< ICFBamChain > iter = subdictPrevRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictPrevRelIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByPrevRelIdx.put( key, subdictPrevRelIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	public ICFBamChain[] readDerivedByNextRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByNextRelIdx";
		CFBamBuffChainByNextRelIdxKey key = schema.getFactoryChain().newNextRelIdxKey();
		key.setRequiredNextRelationId( NextRelationId );

		ICFBamChain[] recArray;
		if( dictByNextRelIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictNextRelIdx
				= dictByNextRelIdx.get( key );
			recArray = new ICFBamChain[ subdictNextRelIdx.size() ];
			Iterator< ICFBamChain > iter = subdictNextRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictNextRelIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByNextRelIdx.put( key, subdictNextRelIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	public ICFBamChain readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryChain().newPKey();
		key.setRequiredId( Id );

		ICFBamChain buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamChain readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamChain.readBuff";
		ICFBamChain buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamChain lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamChain buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamChain[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamChain.readAllBuff";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	public ICFBamChain readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByIdIdx() ";
		ICFBamChain buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
			return( (ICFBamChain)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamChain[] readBuffByChainTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByChainTableIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByChainTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	public ICFBamChain[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByDefSchemaIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	public ICFBamChain readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByUNameIdx() ";
		ICFBamChain buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
			return( (ICFBamChain)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamChain[] readBuffByPrevRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByPrevRelIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByPrevRelIdx( Authorization,
			PrevRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	public ICFBamChain[] readBuffByNextRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByNextRelIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByNextRelIdx( Authorization,
			NextRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	public void updateChain( ICFSecAuthorization Authorization,
		ICFBamChain Buff )
	{
		CFLibDbKeyHash256 pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamChain existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateChain",
				"Existing record not found",
				"Chain",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateChain",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffChainByChainTableIdxKey existingKeyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		existingKeyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffChainByChainTableIdxKey newKeyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		newKeyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffChainByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffChainByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffChainByUNameIdxKey existingKeyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffChainByUNameIdxKey newKeyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffChainByPrevRelIdxKey existingKeyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		existingKeyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamBuffChainByPrevRelIdxKey newKeyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		newKeyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamBuffChainByNextRelIdxKey existingKeyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		existingKeyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		CFBamBuffChainByNextRelIdxKey newKeyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		newKeyNextRelIdx.setRequiredNextRelationId( Buff.getRequiredNextRelationId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateChain",
					"ChainUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPrevRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Lookup",
						"PrevRelation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredNextRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Lookup",
						"NextRelation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByChainTableIdx.get( existingKeyChainTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByChainTableIdx.containsKey( newKeyChainTableIdx ) ) {
			subdict = dictByChainTableIdx.get( newKeyChainTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByChainTableIdx.put( newKeyChainTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByPrevRelIdx.get( existingKeyPrevRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevRelIdx.containsKey( newKeyPrevRelIdx ) ) {
			subdict = dictByPrevRelIdx.get( newKeyPrevRelIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByPrevRelIdx.put( newKeyPrevRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextRelIdx.get( existingKeyNextRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextRelIdx.containsKey( newKeyNextRelIdx ) ) {
			subdict = dictByNextRelIdx.get( newKeyNextRelIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByNextRelIdx.put( newKeyNextRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteChain( ICFSecAuthorization Authorization,
		ICFBamChain Buff )
	{
		final String S_ProcName = "CFBamRamChainTable.deleteChain() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamChain existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteChain",
				pkey );
		}
		CFBamBuffChainByChainTableIdxKey keyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		keyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffChainByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffChainByUNameIdxKey keyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffChainByPrevRelIdxKey keyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamBuffChainByNextRelIdxKey keyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		keyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffChain > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByChainTableIdx.get( keyChainTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByPrevRelIdx.get( keyPrevRelIdx );
		subdict.remove( pkey );

		subdict = dictByNextRelIdx.get( keyNextRelIdx );
		subdict.remove( pkey );

	}
	public void deleteChainByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryChain().newPKey();
		key.setRequiredId( argId );
		deleteChainByIdIdx( Authorization, key );
	}

	public void deleteChainByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamChain cur;
		LinkedList<ICFBamChain> matchSet = new LinkedList<ICFBamChain>();
		Iterator<ICFBamChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByChainTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffChainByChainTableIdxKey key = schema.getFactoryChain().newChainTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteChainByChainTableIdx( Authorization, key );
	}

	public void deleteChainByChainTableIdx( ICFSecAuthorization Authorization,
		ICFBamChainByChainTableIdxKey argKey )
	{
		ICFBamChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamChain> matchSet = new LinkedList<ICFBamChain>();
		Iterator<ICFBamChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffChainByDefSchemaIdxKey key = schema.getFactoryChain().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteChainByDefSchemaIdx( Authorization, key );
	}

	public void deleteChainByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamChainByDefSchemaIdxKey argKey )
	{
		ICFBamChain cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamChain> matchSet = new LinkedList<ICFBamChain>();
		Iterator<ICFBamChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffChainByUNameIdxKey key = schema.getFactoryChain().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteChainByUNameIdx( Authorization, key );
	}

	public void deleteChainByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamChainByUNameIdxKey argKey )
	{
		ICFBamChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamChain> matchSet = new LinkedList<ICFBamChain>();
		Iterator<ICFBamChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByPrevRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevRelationId )
	{
		CFBamBuffChainByPrevRelIdxKey key = schema.getFactoryChain().newPrevRelIdxKey();
		key.setRequiredPrevRelationId( argPrevRelationId );
		deleteChainByPrevRelIdx( Authorization, key );
	}

	public void deleteChainByPrevRelIdx( ICFSecAuthorization Authorization,
		ICFBamChainByPrevRelIdxKey argKey )
	{
		ICFBamChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamChain> matchSet = new LinkedList<ICFBamChain>();
		Iterator<ICFBamChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByNextRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextRelationId )
	{
		CFBamBuffChainByNextRelIdxKey key = schema.getFactoryChain().newNextRelIdxKey();
		key.setRequiredNextRelationId( argNextRelationId );
		deleteChainByNextRelIdx( Authorization, key );
	}

	public void deleteChainByNextRelIdx( ICFSecAuthorization Authorization,
		ICFBamChainByNextRelIdxKey argKey )
	{
		ICFBamChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamChain> matchSet = new LinkedList<ICFBamChain>();
		Iterator<ICFBamChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}
}
