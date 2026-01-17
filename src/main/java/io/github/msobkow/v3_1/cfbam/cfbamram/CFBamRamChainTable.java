
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
 *	CFBamRamChainTable in-memory RAM DbIO implementation
 *	for Chain.
 */
public class CFBamRamChainTable
	implements ICFBamChainTable
{
	private ICFBamSchema schema;
	private Map< CFBamChainPKey,
				CFBamChainBuff > dictByPKey
		= new HashMap< CFBamChainPKey,
				CFBamChainBuff >();
	private Map< CFBamChainByChainTableIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByChainTableIdx
		= new HashMap< CFBamChainByChainTableIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();
	private Map< CFBamChainByDefSchemaIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamChainByDefSchemaIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();
	private Map< CFBamChainByUNameIdxKey,
			CFBamChainBuff > dictByUNameIdx
		= new HashMap< CFBamChainByUNameIdxKey,
			CFBamChainBuff >();
	private Map< CFBamChainByPrevRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByPrevRelIdx
		= new HashMap< CFBamChainByPrevRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();
	private Map< CFBamChainByNextRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByNextRelIdx
		= new HashMap< CFBamChainByNextRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();

	public CFBamRamChainTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createChain( CFSecAuthorization Authorization,
		CFBamChainBuff Buff )
	{
		final String S_ProcName = "createChain";
		CFBamChainPKey pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredId( schema.nextChainIdGen() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamChainByChainTableIdxKey keyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		keyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamChainByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamChainByUNameIdxKey keyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamChainByPrevRelIdxKey keyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamChainByNextRelIdxKey keyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
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

		Map< CFBamChainPKey, CFBamChainBuff > subdictChainTableIdx;
		if( dictByChainTableIdx.containsKey( keyChainTableIdx ) ) {
			subdictChainTableIdx = dictByChainTableIdx.get( keyChainTableIdx );
		}
		else {
			subdictChainTableIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByChainTableIdx.put( keyChainTableIdx, subdictChainTableIdx );
		}
		subdictChainTableIdx.put( pkey, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictPrevRelIdx;
		if( dictByPrevRelIdx.containsKey( keyPrevRelIdx ) ) {
			subdictPrevRelIdx = dictByPrevRelIdx.get( keyPrevRelIdx );
		}
		else {
			subdictPrevRelIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByPrevRelIdx.put( keyPrevRelIdx, subdictPrevRelIdx );
		}
		subdictPrevRelIdx.put( pkey, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictNextRelIdx;
		if( dictByNextRelIdx.containsKey( keyNextRelIdx ) ) {
			subdictNextRelIdx = dictByNextRelIdx.get( keyNextRelIdx );
		}
		else {
			subdictNextRelIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByNextRelIdx.put( keyNextRelIdx, subdictNextRelIdx );
		}
		subdictNextRelIdx.put( pkey, Buff );

	}

	public CFBamChainBuff readDerived( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "CFBamRamChain.readDerived";
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamChainBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff lockDerived( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "CFBamRamChain.readDerived";
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamChainBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamChain.readAllDerived";
		CFBamChainBuff[] retList = new CFBamChainBuff[ dictByPKey.values().size() ];
		Iterator< CFBamChainBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamChainBuff[] readDerivedByChainTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByChainTableIdx";
		CFBamChainByChainTableIdxKey key = schema.getFactoryChain().newChainTableIdxKey();
		key.setRequiredTableId( TableId );

		CFBamChainBuff[] recArray;
		if( dictByChainTableIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictChainTableIdx
				= dictByChainTableIdx.get( key );
			recArray = new CFBamChainBuff[ subdictChainTableIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictChainTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictChainTableIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByChainTableIdx.put( key, subdictChainTableIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByDefSchemaIdx";
		CFBamChainByDefSchemaIdxKey key = schema.getFactoryChain().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamChainBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamChainBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictDefSchemaIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByUNameIdx";
		CFBamChainByUNameIdxKey key = schema.getFactoryChain().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamChainBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff[] readDerivedByPrevRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByPrevRelIdx";
		CFBamChainByPrevRelIdxKey key = schema.getFactoryChain().newPrevRelIdxKey();
		key.setRequiredPrevRelationId( PrevRelationId );

		CFBamChainBuff[] recArray;
		if( dictByPrevRelIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictPrevRelIdx
				= dictByPrevRelIdx.get( key );
			recArray = new CFBamChainBuff[ subdictPrevRelIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictPrevRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictPrevRelIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByPrevRelIdx.put( key, subdictPrevRelIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff[] readDerivedByNextRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByNextRelIdx";
		CFBamChainByNextRelIdxKey key = schema.getFactoryChain().newNextRelIdxKey();
		key.setRequiredNextRelationId( NextRelationId );

		CFBamChainBuff[] recArray;
		if( dictByNextRelIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictNextRelIdx
				= dictByNextRelIdx.get( key );
			recArray = new CFBamChainBuff[ subdictNextRelIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictNextRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictNextRelIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByNextRelIdx.put( key, subdictNextRelIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByIdIdx() ";
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredId( Id );

		CFBamChainBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff readBuff( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "CFBamRamChain.readBuff";
		CFBamChainBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff lockBuff( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamChainBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamChain.readAllBuff";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByIdIdx() ";
		CFBamChainBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
			return( (CFBamChainBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamChainBuff[] readBuffByChainTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByChainTableIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByChainTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByDefSchemaIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByUNameIdx() ";
		CFBamChainBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
			return( (CFBamChainBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamChainBuff[] readBuffByPrevRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByPrevRelIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByPrevRelIdx( Authorization,
			PrevRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff[] readBuffByNextRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByNextRelIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByNextRelIdx( Authorization,
			NextRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public void updateChain( CFSecAuthorization Authorization,
		CFBamChainBuff Buff )
	{
		CFBamChainPKey pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamChainBuff existing = dictByPKey.get( pkey );
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
		CFBamChainByChainTableIdxKey existingKeyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		existingKeyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamChainByChainTableIdxKey newKeyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		newKeyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamChainByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamChainByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamChainByUNameIdxKey existingKeyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamChainByUNameIdxKey newKeyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamChainByPrevRelIdxKey existingKeyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		existingKeyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamChainByPrevRelIdxKey newKeyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		newKeyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamChainByNextRelIdxKey existingKeyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		existingKeyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		CFBamChainByNextRelIdxKey newKeyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
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

		Map< CFBamChainPKey, CFBamChainBuff > subdict;

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
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
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
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
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
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
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
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByNextRelIdx.put( newKeyNextRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteChain( CFSecAuthorization Authorization,
		CFBamChainBuff Buff )
	{
		final String S_ProcName = "CFBamRamChainTable.deleteChain() ";
		String classCode;
		CFBamChainPKey pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamChainBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteChain",
				pkey );
		}
		CFBamChainByChainTableIdxKey keyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		keyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamChainByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamChainByUNameIdxKey keyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamChainByPrevRelIdxKey keyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamChainByNextRelIdxKey keyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		keyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamChainPKey, CFBamChainBuff > subdict;

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
	public void deleteChainByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredId( argId );
		deleteChainByIdIdx( Authorization, key );
	}

	public void deleteChainByIdIdx( CFSecAuthorization Authorization,
		CFBamChainPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamChainBuff cur;
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByChainTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamChainByChainTableIdxKey key = schema.getFactoryChain().newChainTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteChainByChainTableIdx( Authorization, key );
	}

	public void deleteChainByChainTableIdx( CFSecAuthorization Authorization,
		CFBamChainByChainTableIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamChainByDefSchemaIdxKey key = schema.getFactoryChain().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteChainByDefSchemaIdx( Authorization, key );
	}

	public void deleteChainByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamChainByDefSchemaIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamChainByUNameIdxKey key = schema.getFactoryChain().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteChainByUNameIdx( Authorization, key );
	}

	public void deleteChainByUNameIdx( CFSecAuthorization Authorization,
		CFBamChainByUNameIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByPrevRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevRelationId )
	{
		CFBamChainByPrevRelIdxKey key = schema.getFactoryChain().newPrevRelIdxKey();
		key.setRequiredPrevRelationId( argPrevRelationId );
		deleteChainByPrevRelIdx( Authorization, key );
	}

	public void deleteChainByPrevRelIdx( CFSecAuthorization Authorization,
		CFBamChainByPrevRelIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByNextRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextRelationId )
	{
		CFBamChainByNextRelIdxKey key = schema.getFactoryChain().newNextRelIdxKey();
		key.setRequiredNextRelationId( argNextRelationId );
		deleteChainByNextRelIdx( Authorization, key );
	}

	public void deleteChainByNextRelIdx( CFSecAuthorization Authorization,
		CFBamChainByNextRelIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}
}
