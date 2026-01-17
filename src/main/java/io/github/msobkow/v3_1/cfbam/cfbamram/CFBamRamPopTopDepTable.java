
// Description: Java 25 in-memory RAM DbIO implementation for PopTopDep.

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
 *	CFBamRamPopTopDepTable in-memory RAM DbIO implementation
 *	for PopTopDep.
 */
public class CFBamRamPopTopDepTable
	implements ICFBamPopTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamPopTopDepBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamPopTopDepBuff >();
	private Map< CFBamPopTopDepByContRelIdxKey,
				Map< CFBamScopePKey,
					CFBamPopTopDepBuff >> dictByContRelIdx
		= new HashMap< CFBamPopTopDepByContRelIdxKey,
				Map< CFBamScopePKey,
					CFBamPopTopDepBuff >>();
	private Map< CFBamPopTopDepByUNameIdxKey,
			CFBamPopTopDepBuff > dictByUNameIdx
		= new HashMap< CFBamPopTopDepByUNameIdxKey,
			CFBamPopTopDepBuff >();

	public CFBamRamPopTopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createPopTopDep( CFSecAuthorization Authorization,
		CFBamPopTopDepBuff Buff )
	{
		final String S_ProcName = "createPopTopDep";
		schema.getTablePopDep().createPopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopTopDepByContRelIdxKey keyContRelIdx = schema.getFactoryPopTopDep().newContRelIdxKey();
		keyContRelIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );

		CFBamPopTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryPopTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopTopDepUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredContRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ContRelation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamPopTopDepBuff > subdictContRelIdx;
		if( dictByContRelIdx.containsKey( keyContRelIdx ) ) {
			subdictContRelIdx = dictByContRelIdx.get( keyContRelIdx );
		}
		else {
			subdictContRelIdx = new HashMap< CFBamScopePKey, CFBamPopTopDepBuff >();
			dictByContRelIdx.put( keyContRelIdx, subdictContRelIdx );
		}
		subdictContRelIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamPopTopDepBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopTopDepBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopTopDepBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopTopDep.readAllDerived";
		CFBamPopTopDepBuff[] retList = new CFBamPopTopDepBuff[ dictByPKey.values().size() ];
		Iterator< CFBamPopTopDepBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamPopTopDepBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopTopDepBuff ) ) {
					filteredList.add( (CFBamPopTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
		}
	}

	public CFBamPopTopDepBuff[] readDerivedByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		CFBamPopDepBuff buffList[] = schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamPopDepBuff buff;
			ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopTopDepBuff ) ) {
					filteredList.add( (CFBamPopTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
		}
	}

	public CFBamPopTopDepBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		CFBamPopDepBuff buffList[] = schema.getTablePopDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamPopDepBuff buff;
			ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopTopDepBuff ) ) {
					filteredList.add( (CFBamPopTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
		}
	}

	public CFBamPopTopDepBuff[] readDerivedByContRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readDerivedByContRelIdx";
		CFBamPopTopDepByContRelIdxKey key = schema.getFactoryPopTopDep().newContRelIdxKey();
		key.setRequiredContRelationId( ContRelationId );

		CFBamPopTopDepBuff[] recArray;
		if( dictByContRelIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamPopTopDepBuff > subdictContRelIdx
				= dictByContRelIdx.get( key );
			recArray = new CFBamPopTopDepBuff[ subdictContRelIdx.size() ];
			Iterator< CFBamPopTopDepBuff > iter = subdictContRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamPopTopDepBuff > subdictContRelIdx
				= new HashMap< CFBamScopePKey, CFBamPopTopDepBuff >();
			dictByContRelIdx.put( key, subdictContRelIdx );
			recArray = new CFBamPopTopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamPopTopDepBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readDerivedByUNameIdx";
		CFBamPopTopDepByUNameIdxKey key = schema.getFactoryPopTopDep().newUNameIdxKey();
		key.setRequiredContRelationId( ContRelationId );
		key.setRequiredName( Name );

		CFBamPopTopDepBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopTopDepBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamPopTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopTopDepBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readBuff";
		CFBamPopTopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a834" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopTopDepBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamPopTopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a834" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopTopDepBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readAllBuff";
		CFBamPopTopDepBuff buff;
		ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
		CFBamPopTopDepBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a834" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
	}

	public CFBamPopTopDepBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamPopTopDepBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamPopTopDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamPopTopDepBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamPopTopDepBuff buff;
		ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
		CFBamPopTopDepBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamPopTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
	}

	public CFBamPopTopDepBuff[] readBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		CFBamPopTopDepBuff buff;
		ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
		CFBamPopTopDepBuff[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
	}

	public CFBamPopTopDepBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		CFBamPopTopDepBuff buff;
		ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
		CFBamPopTopDepBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
	}

	public CFBamPopTopDepBuff[] readBuffByContRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readBuffByContRelIdx() ";
		CFBamPopTopDepBuff buff;
		ArrayList<CFBamPopTopDepBuff> filteredList = new ArrayList<CFBamPopTopDepBuff>();
		CFBamPopTopDepBuff[] buffList = readDerivedByContRelIdx( Authorization,
			ContRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a834" ) ) {
				filteredList.add( (CFBamPopTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopTopDepBuff[0] ) );
	}

	public CFBamPopTopDepBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readBuffByUNameIdx() ";
		CFBamPopTopDepBuff buff = readDerivedByUNameIdx( Authorization,
			ContRelationId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a834" ) ) {
			return( (CFBamPopTopDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific PopTopDep buffer instances identified by the duplicate key RelationIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The PopTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopTopDepBuff[] pageBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRelationIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopTopDep buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The PopTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopTopDepBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopTopDep buffer instances identified by the duplicate key ContRelIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	ContRelationId	The PopTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopTopDepBuff[] pageBuffByContRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByContRelIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updatePopTopDep( CFSecAuthorization Authorization,
		CFBamPopTopDepBuff Buff )
	{
		schema.getTablePopDep().updatePopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopTopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopTopDep",
				"Existing record not found",
				"PopTopDep",
				pkey );
		}
		CFBamPopTopDepByContRelIdxKey existingKeyContRelIdx = schema.getFactoryPopTopDep().newContRelIdxKey();
		existingKeyContRelIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );

		CFBamPopTopDepByContRelIdxKey newKeyContRelIdx = schema.getFactoryPopTopDep().newContRelIdxKey();
		newKeyContRelIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );

		CFBamPopTopDepByUNameIdxKey existingKeyUNameIdx = schema.getFactoryPopTopDep().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamPopTopDepByUNameIdxKey newKeyUNameIdx = schema.getFactoryPopTopDep().newUNameIdxKey();
		newKeyUNameIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopTopDep",
					"PopTopDepUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopTopDep",
						"Superclass",
						"SuperClass",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredContRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopTopDep",
						"Container",
						"ContRelation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamPopTopDepBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByContRelIdx.get( existingKeyContRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContRelIdx.containsKey( newKeyContRelIdx ) ) {
			subdict = dictByContRelIdx.get( newKeyContRelIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamPopTopDepBuff >();
			dictByContRelIdx.put( newKeyContRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deletePopTopDep( CFSecAuthorization Authorization,
		CFBamPopTopDepBuff Buff )
	{
		final String S_ProcName = "CFBamRamPopTopDepTable.deletePopTopDep() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopTopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopTopDep",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckPopDep[] = schema.getTablePopSubDep1().readDerivedByPopTopDepIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckPopDep.length > 0 ) {
			schema.getTablePopSubDep1().deletePopSubDep1ByPopTopDepIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamPopTopDepByContRelIdxKey keyContRelIdx = schema.getFactoryPopTopDep().newContRelIdxKey();
		keyContRelIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );

		CFBamPopTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryPopTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamPopTopDepBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByContRelIdx.get( keyContRelIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	public void deletePopTopDepByContRelIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argContRelationId )
	{
		CFBamPopTopDepByContRelIdxKey key = schema.getFactoryPopTopDep().newContRelIdxKey();
		key.setRequiredContRelationId( argContRelationId );
		deletePopTopDepByContRelIdx( Authorization, key );
	}

	public void deletePopTopDepByContRelIdx( CFSecAuthorization Authorization,
		CFBamPopTopDepByContRelIdxKey argKey )
	{
		CFBamPopTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopTopDepBuff> matchSet = new LinkedList<CFBamPopTopDepBuff>();
		Iterator<CFBamPopTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopTopDep( Authorization, cur );
		}
	}

	public void deletePopTopDepByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argContRelationId,
		String argName )
	{
		CFBamPopTopDepByUNameIdxKey key = schema.getFactoryPopTopDep().newUNameIdxKey();
		key.setRequiredContRelationId( argContRelationId );
		key.setRequiredName( argName );
		deletePopTopDepByUNameIdx( Authorization, key );
	}

	public void deletePopTopDepByUNameIdx( CFSecAuthorization Authorization,
		CFBamPopTopDepByUNameIdxKey argKey )
	{
		CFBamPopTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopTopDepBuff> matchSet = new LinkedList<CFBamPopTopDepBuff>();
		Iterator<CFBamPopTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopTopDep( Authorization, cur );
		}
	}

	public void deletePopTopDepByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopTopDepByRelationIdx( Authorization, key );
	}

	public void deletePopTopDepByRelationIdx( CFSecAuthorization Authorization,
		CFBamPopDepByRelationIdxKey argKey )
	{
		CFBamPopTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopTopDepBuff> matchSet = new LinkedList<CFBamPopTopDepBuff>();
		Iterator<CFBamPopTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopTopDep( Authorization, cur );
		}
	}

	public void deletePopTopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopTopDepByDefSchemaIdx( Authorization, key );
	}

	public void deletePopTopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamPopTopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopTopDepBuff> matchSet = new LinkedList<CFBamPopTopDepBuff>();
		Iterator<CFBamPopTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopTopDep( Authorization, cur );
		}
	}

	public void deletePopTopDepByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deletePopTopDepByIdIdx( Authorization, key );
	}

	public void deletePopTopDepByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamPopTopDepBuff cur;
		LinkedList<CFBamPopTopDepBuff> matchSet = new LinkedList<CFBamPopTopDepBuff>();
		Iterator<CFBamPopTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopTopDep( Authorization, cur );
		}
	}

	public void deletePopTopDepByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopTopDepByTenantIdx( Authorization, key );
	}

	public void deletePopTopDepByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamPopTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopTopDepBuff> matchSet = new LinkedList<CFBamPopTopDepBuff>();
		Iterator<CFBamPopTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopTopDep( Authorization, cur );
		}
	}
}
