
// Description: Java 25 in-memory RAM DbIO implementation for PopSubDep1.

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
 *	CFBamRamPopSubDep1Table in-memory RAM DbIO implementation
 *	for PopSubDep1.
 */
public class CFBamRamPopSubDep1Table
	implements ICFBamPopSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamPopSubDep1Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamPopSubDep1Buff >();
	private Map< CFBamPopSubDep1ByPopTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep1Buff >> dictByPopTopDepIdx
		= new HashMap< CFBamPopSubDep1ByPopTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep1Buff >>();
	private Map< CFBamPopSubDep1ByUNameIdxKey,
			CFBamPopSubDep1Buff > dictByUNameIdx
		= new HashMap< CFBamPopSubDep1ByUNameIdxKey,
			CFBamPopSubDep1Buff >();

	public CFBamRamPopSubDep1Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createPopSubDep1( CFSecAuthorization Authorization,
		CFBamPopSubDep1Buff Buff )
	{
		final String S_ProcName = "createPopSubDep1";
		schema.getTablePopDep().createPopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep1ByPopTopDepIdxKey keyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		keyPopTopDepIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep1UNameIdx",
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
				if( null == schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"PopTopDep",
						"PopTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdictPopTopDepIdx;
		if( dictByPopTopDepIdx.containsKey( keyPopTopDepIdx ) ) {
			subdictPopTopDepIdx = dictByPopTopDepIdx.get( keyPopTopDepIdx );
		}
		else {
			subdictPopTopDepIdx = new HashMap< CFBamScopePKey, CFBamPopSubDep1Buff >();
			dictByPopTopDepIdx.put( keyPopTopDepIdx, subdictPopTopDepIdx );
		}
		subdictPopTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamPopSubDep1Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep1.readAllDerived";
		CFBamPopSubDep1Buff[] retList = new CFBamPopSubDep1Buff[ dictByPKey.values().size() ];
		Iterator< CFBamPopSubDep1Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamPopSubDep1Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep1Buff ) ) {
					filteredList.add( (CFBamPopSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
		}
	}

	public CFBamPopSubDep1Buff[] readDerivedByRelationIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep1Buff ) ) {
					filteredList.add( (CFBamPopSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
		}
	}

	public CFBamPopSubDep1Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep1Buff ) ) {
					filteredList.add( (CFBamPopSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
		}
	}

	public CFBamPopSubDep1Buff[] readDerivedByPopTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerivedByPopTopDepIdx";
		CFBamPopSubDep1ByPopTopDepIdxKey key = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		key.setRequiredPopTopDepId( PopTopDepId );

		CFBamPopSubDep1Buff[] recArray;
		if( dictByPopTopDepIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdictPopTopDepIdx
				= dictByPopTopDepIdx.get( key );
			recArray = new CFBamPopSubDep1Buff[ subdictPopTopDepIdx.size() ];
			Iterator< CFBamPopSubDep1Buff > iter = subdictPopTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdictPopTopDepIdx
				= new HashMap< CFBamScopePKey, CFBamPopSubDep1Buff >();
			dictByPopTopDepIdx.put( key, subdictPopTopDepIdx );
			recArray = new CFBamPopSubDep1Buff[0];
		}
		return( recArray );
	}

	public CFBamPopSubDep1Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerivedByUNameIdx";
		CFBamPopSubDep1ByUNameIdxKey key = schema.getFactoryPopSubDep1().newUNameIdxKey();
		key.setRequiredPopTopDepId( PopTopDepId );
		key.setRequiredName( Name );

		CFBamPopSubDep1Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamPopSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readBuff";
		CFBamPopSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a831" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamPopSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a831" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readAllBuff";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a831" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamPopSubDep1Buff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamPopSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamPopSubDep1Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff[] readBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff[] readBuffByPopTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readBuffByPopTopDepIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByPopTopDepIdx( Authorization,
			PopTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a831" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readBuffByUNameIdx() ";
		CFBamPopSubDep1Buff buff = readDerivedByUNameIdx( Authorization,
			PopTopDepId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a831" ) ) {
			return( (CFBamPopSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific PopSubDep1 buffer instances identified by the duplicate key RelationIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep1Buff[] pageBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRelationIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep1 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep1Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep1 buffer instances identified by the duplicate key PopTopDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PopTopDepId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep1Buff[] pageBuffByPopTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByPopTopDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updatePopSubDep1( CFSecAuthorization Authorization,
		CFBamPopSubDep1Buff Buff )
	{
		schema.getTablePopDep().updatePopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep1",
				"Existing record not found",
				"PopSubDep1",
				pkey );
		}
		CFBamPopSubDep1ByPopTopDepIdxKey existingKeyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		existingKeyPopTopDepIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByPopTopDepIdxKey newKeyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		newKeyPopTopDepIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamPopSubDep1ByUNameIdxKey newKeyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		newKeyUNameIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep1",
					"PopSubDep1UNameIdx",
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
						"updatePopSubDep1",
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
				if( null == schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep1",
						"Container",
						"PopTopDep",
						"PopTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopTopDepIdx.get( existingKeyPopTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopTopDepIdx.containsKey( newKeyPopTopDepIdx ) ) {
			subdict = dictByPopTopDepIdx.get( newKeyPopTopDepIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamPopSubDep1Buff >();
			dictByPopTopDepIdx.put( newKeyPopTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deletePopSubDep1( CFSecAuthorization Authorization,
		CFBamPopSubDep1Buff Buff )
	{
		final String S_ProcName = "CFBamRamPopSubDep1Table.deletePopSubDep1() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckPopDep[] = schema.getTablePopSubDep2().readDerivedByPopSubDep1Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckPopDep.length > 0 ) {
			schema.getTablePopSubDep2().deletePopSubDep2ByPopSubDep1Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamPopSubDep1ByPopTopDepIdxKey keyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		keyPopTopDepIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopTopDepIdx.get( keyPopTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	public void deletePopSubDep1ByPopTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopTopDepId )
	{
		CFBamPopSubDep1ByPopTopDepIdxKey key = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		key.setRequiredPopTopDepId( argPopTopDepId );
		deletePopSubDep1ByPopTopDepIdx( Authorization, key );
	}

	public void deletePopSubDep1ByPopTopDepIdx( CFSecAuthorization Authorization,
		CFBamPopSubDep1ByPopTopDepIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopTopDepId,
		String argName )
	{
		CFBamPopSubDep1ByUNameIdxKey key = schema.getFactoryPopSubDep1().newUNameIdxKey();
		key.setRequiredPopTopDepId( argPopTopDepId );
		key.setRequiredName( argName );
		deletePopSubDep1ByUNameIdx( Authorization, key );
	}

	public void deletePopSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		CFBamPopSubDep1ByUNameIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep1ByRelationIdx( Authorization, key );
	}

	public void deletePopSubDep1ByRelationIdx( CFSecAuthorization Authorization,
		CFBamPopDepByRelationIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep1ByDefSchemaIdx( Authorization, key );
	}

	public void deletePopSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deletePopSubDep1ByIdIdx( Authorization, key );
	}

	public void deletePopSubDep1ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamPopSubDep1Buff cur;
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep1ByTenantIdx( Authorization, key );
	}

	public void deletePopSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}
}
