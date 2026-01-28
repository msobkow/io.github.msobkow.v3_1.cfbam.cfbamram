
// Description: Java 25 in-memory RAM DbIO implementation for DelSubDep2.

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
 *	CFBamRamDelSubDep2Table in-memory RAM DbIO implementation
 *	for DelSubDep2.
 */
public class CFBamRamDelSubDep2Table
	implements ICFBamDelSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelSubDep2 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelSubDep2 >();
	private Map< CFBamBuffDelSubDep2ByContDelDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep2 >> dictByContDelDep1Idx
		= new HashMap< CFBamBuffDelSubDep2ByContDelDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep2 >>();
	private Map< CFBamBuffDelSubDep2ByUNameIdxKey,
			CFBamBuffDelSubDep2 > dictByUNameIdx
		= new HashMap< CFBamBuffDelSubDep2ByUNameIdxKey,
			CFBamBuffDelSubDep2 >();

	public CFBamRamDelSubDep2Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamDelSubDep2 createDelSubDep2( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2 iBuff )
	{
		final String S_ProcName = "createDelSubDep2";
		
		CFBamBuffDelSubDep2 Buff = (CFBamBuffDelSubDep2)(schema.getTableDelDep().createDelDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffDelSubDep2ByContDelDep1IdxKey keyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getFactoryDelSubDep2().newByContDelDep1IdxKey();
		keyContDelDep1Idx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getFactoryDelSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelSubDep2UNameIdx",
				"DelSubDep2UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"DelSubDep1",
						"DelSubDep1",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdictContDelDep1Idx;
		if( dictByContDelDep1Idx.containsKey( keyContDelDep1Idx ) ) {
			subdictContDelDep1Idx = dictByContDelDep1Idx.get( keyContDelDep1Idx );
		}
		else {
			subdictContDelDep1Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep2 >();
			dictByContDelDep1Idx.put( keyContDelDep1Idx, subdictContDelDep1Idx );
		}
		subdictContDelDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamDelSubDep2.CLASS_CODE) {
				CFBamBuffDelSubDep2 retbuff = ((CFBamBuffDelSubDep2)(schema.getFactoryDelSubDep2().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamDelSubDep2 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerived";
		ICFBamDelSubDep2 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelSubDep2 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerived";
		ICFBamDelSubDep2 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelSubDep2[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelSubDep2.readAllDerived";
		ICFBamDelSubDep2[] retList = new ICFBamDelSubDep2[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDelSubDep2 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamDelSubDep2[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep2 ) ) {
					filteredList.add( (ICFBamDelSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
		}
	}

	public ICFBamDelSubDep2[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep2 ) ) {
					filteredList.add( (ICFBamDelSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
		}
	}

	public ICFBamDelSubDep2[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep2 ) ) {
					filteredList.add( (ICFBamDelSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
		}
	}

	public ICFBamDelSubDep2[] readDerivedByContDelDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerivedByContDelDep1Idx";
		CFBamBuffDelSubDep2ByContDelDep1IdxKey key = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getFactoryDelSubDep2().newByContDelDep1IdxKey();
		key.setRequiredDelSubDep1Id( DelSubDep1Id );

		ICFBamDelSubDep2[] recArray;
		if( dictByContDelDep1Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdictContDelDep1Idx
				= dictByContDelDep1Idx.get( key );
			recArray = new ICFBamDelSubDep2[ subdictContDelDep1Idx.size() ];
			Iterator< CFBamBuffDelSubDep2 > iter = subdictContDelDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdictContDelDep1Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep2 >();
			dictByContDelDep1Idx.put( key, subdictContDelDep1Idx );
			recArray = new ICFBamDelSubDep2[0];
		}
		return( recArray );
	}

	public ICFBamDelSubDep2 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerivedByUNameIdx";
		CFBamBuffDelSubDep2ByUNameIdxKey key = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getFactoryDelSubDep2().newByUNameIdxKey();
		key.setRequiredDelSubDep1Id( DelSubDep1Id );
		key.setRequiredName( Name );

		ICFBamDelSubDep2 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelSubDep2 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamDelSubDep2 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelSubDep2 readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readBuff";
		ICFBamDelSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelSubDep2 lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamDelSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelSubDep2[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readAllBuff";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep2.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	public ICFBamDelSubDep2 readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamDelSubDep2 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamDelSubDep2[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	public ICFBamDelSubDep2[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	public ICFBamDelSubDep2[] readBuffByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	public ICFBamDelSubDep2[] readBuffByContDelDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readBuffByContDelDep1Idx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByContDelDep1Idx( Authorization,
			DelSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep2.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	public ICFBamDelSubDep2 readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readBuffByUNameIdx() ";
		ICFBamDelSubDep2 buff = readDerivedByUNameIdx( Authorization,
			DelSubDep1Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep2.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific DelSubDep2 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamDelSubDep2[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep2 buffer instances identified by the duplicate key DelDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamDelSubDep2[] pageBuffByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDelDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep2 buffer instances identified by the duplicate key ContDelDep1Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DelSubDep1Id	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamDelSubDep2[] pageBuffByContDelDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByContDelDep1Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFBamDelSubDep2 updateDelSubDep2( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2 iBuff )
	{
		CFBamBuffDelSubDep2 Buff = (CFBamBuffDelSubDep2)schema.getTableDelDep().updateDelDep( Authorization,	Buff );
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffDelSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelSubDep2",
				"Existing record not found",
				"Existing record not found",
				"DelSubDep2",
				"DelSubDep2",
				pkey );
		}
		CFBamBuffDelSubDep2ByContDelDep1IdxKey existingKeyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getFactoryDelSubDep2().newByContDelDep1IdxKey();
		existingKeyContDelDep1Idx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByContDelDep1IdxKey newKeyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getFactoryDelSubDep2().newByContDelDep1IdxKey();
		newKeyContDelDep1Idx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getFactoryDelSubDep2().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelSubDep2ByUNameIdxKey newKeyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getFactoryDelSubDep2().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelSubDep2",
					"DelSubDep2UNameIdx",
					"DelSubDep2UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep2",
						"Superclass",
						"SuperClass",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep2",
						"Container",
						"DelSubDep1",
						"DelSubDep1",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByContDelDep1Idx.get( existingKeyContDelDep1Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContDelDep1Idx.containsKey( newKeyContDelDep1Idx ) ) {
			subdict = dictByContDelDep1Idx.get( newKeyContDelDep1Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep2 >();
			dictByContDelDep1Idx.put( newKeyContDelDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	public void deleteDelSubDep2( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2 iBuff )
	{
		final String S_ProcName = "CFBamRamDelSubDep2Table.deleteDelSubDep2() ";
		CFBamBuffDelSubDep2 Buff = ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDelSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelSubDep2",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep3().readDerivedByDelSubDep2Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep3().deleteDelSubDep3ByDelSubDep2Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffDelSubDep2ByContDelDep1IdxKey keyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getFactoryDelSubDep2().newByContDelDep1IdxKey();
		keyContDelDep1Idx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getFactoryDelSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByContDelDep1Idx.get( keyContDelDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	public void deleteDelSubDep2ByContDelDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelSubDep1Id )
	{
		CFBamBuffDelSubDep2ByContDelDep1IdxKey key = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getFactoryDelSubDep2().newByContDelDep1IdxKey();
		key.setRequiredDelSubDep1Id( argDelSubDep1Id );
		deleteDelSubDep2ByContDelDep1Idx( Authorization, key );
	}

	public void deleteDelSubDep2ByContDelDep1Idx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2ByContDelDep1IdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelSubDep1Id,
		String argName )
	{
		CFBamBuffDelSubDep2ByUNameIdxKey key = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getFactoryDelSubDep2().newByUNameIdxKey();
		key.setRequiredDelSubDep1Id( argDelSubDep1Id );
		key.setRequiredName( argName );
		deleteDelSubDep2ByUNameIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2ByUNameIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelSubDep2ByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelSubDep2ByDelDepIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDelSubDep2 cur;
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelSubDep2ByTenantIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}
}
