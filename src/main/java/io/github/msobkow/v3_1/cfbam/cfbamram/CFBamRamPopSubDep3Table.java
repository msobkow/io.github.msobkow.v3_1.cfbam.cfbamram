
// Description: Java 25 in-memory RAM DbIO implementation for PopSubDep3.

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
 *	CFBamRamPopSubDep3Table in-memory RAM DbIO implementation
 *	for PopSubDep3.
 */
public class CFBamRamPopSubDep3Table
	implements ICFBamPopSubDep3Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffPopSubDep3 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffPopSubDep3 >();
	private Map< CFBamBuffPopSubDep3ByPopSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep3 >> dictByPopSubDep2Idx
		= new HashMap< CFBamBuffPopSubDep3ByPopSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep3 >>();
	private Map< CFBamBuffPopSubDep3ByUNameIdxKey,
			CFBamBuffPopSubDep3 > dictByUNameIdx
		= new HashMap< CFBamBuffPopSubDep3ByUNameIdxKey,
			CFBamBuffPopSubDep3 >();

	public CFBamRamPopSubDep3Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamPopSubDep3 createPopSubDep3( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3 iBuff )
	{
		final String S_ProcName = "createPopSubDep3";
		
		CFBamBuffPopSubDep3 Buff = (CFBamBuffPopSubDep3)(schema.getTablePopDep().createPopDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey keyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		keyPopSubDep2Idx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getFactoryPopSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep3UNameIdx",
				"PopSubDep3UNameIdx",
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
				if( null == schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"PopSubDep2",
						"PopSubDep2",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdictPopSubDep2Idx;
		if( dictByPopSubDep2Idx.containsKey( keyPopSubDep2Idx ) ) {
			subdictPopSubDep2Idx = dictByPopSubDep2Idx.get( keyPopSubDep2Idx );
		}
		else {
			subdictPopSubDep2Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep3 >();
			dictByPopSubDep2Idx.put( keyPopSubDep2Idx, subdictPopSubDep2Idx );
		}
		subdictPopSubDep2Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamPopSubDep3.CLASS_CODE) {
				CFBamBuffPopSubDep3 retbuff = ((CFBamBuffPopSubDep3)(schema.getFactoryPopSubDep3().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-create-buff-cloning-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamPopSubDep3 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerived";
		ICFBamPopSubDep3 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopSubDep3 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerived";
		ICFBamPopSubDep3 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopSubDep3[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep3.readAllDerived";
		ICFBamPopSubDep3[] retList = new ICFBamPopSubDep3[ dictByPKey.values().size() ];
		Iterator< ICFBamPopSubDep3 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamPopSubDep3[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		ICFBamScope buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamScope buff;
			ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep3 ) ) {
					filteredList.add( (ICFBamPopSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
		}
	}

	public ICFBamPopSubDep3[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		ICFBamPopDep buffList[] = schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamPopDep buff;
			ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep3 ) ) {
					filteredList.add( (ICFBamPopSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
		}
	}

	public ICFBamPopSubDep3[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		ICFBamPopDep buffList[] = schema.getTablePopDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamPopDep buff;
			ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep3 ) ) {
					filteredList.add( (ICFBamPopSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
		}
	}

	public ICFBamPopSubDep3[] readDerivedByPopSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerivedByPopSubDep2Idx";
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey key = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		key.setRequiredPopSubDep2Id( PopSubDep2Id );

		ICFBamPopSubDep3[] recArray;
		if( dictByPopSubDep2Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdictPopSubDep2Idx
				= dictByPopSubDep2Idx.get( key );
			recArray = new ICFBamPopSubDep3[ subdictPopSubDep2Idx.size() ];
			Iterator< ICFBamPopSubDep3 > iter = subdictPopSubDep2Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdictPopSubDep2Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep3 >();
			dictByPopSubDep2Idx.put( key, subdictPopSubDep2Idx );
			recArray = new ICFBamPopSubDep3[0];
		}
		return( recArray );
	}

	public ICFBamPopSubDep3 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerivedByUNameIdx";
		CFBamBuffPopSubDep3ByUNameIdxKey key = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getFactoryPopSubDep3().newByUNameIdxKey();
		key.setRequiredPopSubDep2Id( PopSubDep2Id );
		key.setRequiredName( Name );

		ICFBamPopSubDep3 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopSubDep3 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamPopSubDep3 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopSubDep3 readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readBuff";
		ICFBamPopSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopSubDep3 lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamPopSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopSubDep3[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readAllBuff";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep3.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	public ICFBamPopSubDep3 readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamPopSubDep3 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamPopSubDep3[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	public ICFBamPopSubDep3[] readBuffByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	public ICFBamPopSubDep3[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	public ICFBamPopSubDep3[] readBuffByPopSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readBuffByPopSubDep2Idx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByPopSubDep2Idx( Authorization,
			PopSubDep2Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep3.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	public ICFBamPopSubDep3 readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readBuffByUNameIdx() ";
		ICFBamPopSubDep3 buff = readDerivedByUNameIdx( Authorization,
			PopSubDep2Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep3.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific PopSubDep3 buffer instances identified by the duplicate key RelationIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamPopSubDep3[] pageBuffByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRelationIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep3 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamPopSubDep3[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep3 buffer instances identified by the duplicate key PopSubDep2Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PopSubDep2Id	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamPopSubDep3[] pageBuffByPopSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByPopSubDep2Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFBamPopSubDep3 updatePopSubDep3( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3 Buff )
	{
		ICFBamPopSubDep3 repl = schema.getTablePopDep().updatePopDep( Authorization,
			Buff );
		if (repl != Buff) {
			throw new CFLibInvalidStateException(getClass(), S_ProcName, "repl != Buff", "repl != Buff");
		}
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamPopSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep3",
				"Existing record not found",
				"PopSubDep3",
				pkey );
		}
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey existingKeyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		existingKeyPopSubDep2Idx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByPopSubDep2IdxKey newKeyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		newKeyPopSubDep2Idx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getFactoryPopSubDep3().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffPopSubDep3ByUNameIdxKey newKeyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getFactoryPopSubDep3().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep3",
					"PopSubDep3UNameIdx",
					"PopSubDep3UNameIdx",
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
						"updatePopSubDep3",
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
				if( null == schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep3",
						"Container",
						"PopSubDep2",
						"PopSubDep2",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopSubDep2Idx.get( existingKeyPopSubDep2Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopSubDep2Idx.containsKey( newKeyPopSubDep2Idx ) ) {
			subdict = dictByPopSubDep2Idx.get( newKeyPopSubDep2Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep3 >();
			dictByPopSubDep2Idx.put( newKeyPopSubDep2Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	public void deletePopSubDep3( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3 Buff )
	{
		final String S_ProcName = "CFBamRamPopSubDep3Table.deletePopSubDep3() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamPopSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep3",
				pkey );
		}
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey keyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		keyPopSubDep2Idx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getFactoryPopSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopSubDep2Idx.get( keyPopSubDep2Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	public void deletePopSubDep3ByPopSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep2Id )
	{
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey key = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		key.setRequiredPopSubDep2Id( argPopSubDep2Id );
		deletePopSubDep3ByPopSubDep2Idx( Authorization, key );
	}

	public void deletePopSubDep3ByPopSubDep2Idx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3ByPopSubDep2IdxKey argKey )
	{
		ICFBamPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopSubDep3> matchSet = new LinkedList<ICFBamPopSubDep3>();
		Iterator<ICFBamPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep2Id,
		String argName )
	{
		CFBamBuffPopSubDep3ByUNameIdxKey key = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getFactoryPopSubDep3().newByUNameIdxKey();
		key.setRequiredPopSubDep2Id( argPopSubDep2Id );
		key.setRequiredName( argName );
		deletePopSubDep3ByUNameIdx( Authorization, key );
	}

	public void deletePopSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3ByUNameIdxKey argKey )
	{
		ICFBamPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopSubDep3> matchSet = new LinkedList<ICFBamPopSubDep3>();
		Iterator<ICFBamPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffPopDepByRelationIdxKey key = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep3ByRelationIdx( Authorization, key );
	}

	public void deletePopSubDep3ByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByRelationIdxKey argKey )
	{
		ICFBamPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopSubDep3> matchSet = new LinkedList<ICFBamPopSubDep3>();
		Iterator<ICFBamPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffPopDepByDefSchemaIdxKey key = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep3ByDefSchemaIdx( Authorization, key );
	}

	public void deletePopSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByDefSchemaIdxKey argKey )
	{
		ICFBamPopSubDep3 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopSubDep3> matchSet = new LinkedList<ICFBamPopSubDep3>();
		Iterator<ICFBamPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamPopSubDep3 cur;
		LinkedList<ICFBamPopSubDep3> matchSet = new LinkedList<ICFBamPopSubDep3>();
		Iterator<ICFBamPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep3ByTenantIdx( Authorization, key );
	}

	public void deletePopSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopSubDep3> matchSet = new LinkedList<ICFBamPopSubDep3>();
		Iterator<ICFBamPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}
}
