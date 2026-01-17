
// Description: Java 25 in-memory RAM DbIO implementation for ClearDep.

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
 *	CFBamRamClearDepTable in-memory RAM DbIO implementation
 *	for ClearDep.
 */
public class CFBamRamClearDepTable
	implements ICFBamClearDepTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamClearDepBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamClearDepBuff >();
	private Map< CFBamClearDepByClearDepIdxKey,
				Map< CFBamScopePKey,
					CFBamClearDepBuff >> dictByClearDepIdx
		= new HashMap< CFBamClearDepByClearDepIdxKey,
				Map< CFBamScopePKey,
					CFBamClearDepBuff >>();
	private Map< CFBamClearDepByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamClearDepBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamClearDepByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamClearDepBuff >>();

	public CFBamRamClearDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearDep( CFSecAuthorization Authorization,
		CFBamClearDepBuff Buff )
	{
		final String S_ProcName = "createClearDep";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearDepByClearDepIdxKey keyClearDepIdx = schema.getFactoryClearDep().newClearDepIdxKey();
		keyClearDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamClearDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryClearDep().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
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
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearDepBuff > subdictClearDepIdx;
		if( dictByClearDepIdx.containsKey( keyClearDepIdx ) ) {
			subdictClearDepIdx = dictByClearDepIdx.get( keyClearDepIdx );
		}
		else {
			subdictClearDepIdx = new HashMap< CFBamScopePKey, CFBamClearDepBuff >();
			dictByClearDepIdx.put( keyClearDepIdx, subdictClearDepIdx );
		}
		subdictClearDepIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearDepBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamClearDepBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public CFBamClearDepBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearDepBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearDepBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearDep.readAllDerived";
		CFBamClearDepBuff[] retList = new CFBamClearDepBuff[ dictByPKey.values().size() ];
		Iterator< CFBamClearDepBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamClearDepBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearDepBuff> filteredList = new ArrayList<CFBamClearDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearDepBuff ) ) {
					filteredList.add( (CFBamClearDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearDepBuff[0] ) );
		}
	}

	public CFBamClearDepBuff[] readDerivedByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		CFBamClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( RelationId );

		CFBamClearDepBuff[] recArray;
		if( dictByClearDepIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearDepBuff > subdictClearDepIdx
				= dictByClearDepIdx.get( key );
			recArray = new CFBamClearDepBuff[ subdictClearDepIdx.size() ];
			Iterator< CFBamClearDepBuff > iter = subdictClearDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearDepBuff > subdictClearDepIdx
				= new HashMap< CFBamScopePKey, CFBamClearDepBuff >();
			dictByClearDepIdx.put( key, subdictClearDepIdx );
			recArray = new CFBamClearDepBuff[0];
		}
		return( recArray );
	}

	public CFBamClearDepBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		CFBamClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamClearDepBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearDepBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamClearDepBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamClearDepBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearDepBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamClearDepBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamClearDepBuff[0];
		}
		return( recArray );
	}

	public CFBamClearDepBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamClearDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearDepBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuff";
		CFBamClearDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a810" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearDepBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamClearDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a810" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearDepBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearDep.readAllBuff";
		CFBamClearDepBuff buff;
		ArrayList<CFBamClearDepBuff> filteredList = new ArrayList<CFBamClearDepBuff>();
		CFBamClearDepBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamClearDepBuff[0] ) );
	}

	public CFBamClearDepBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamClearDepBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamClearDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamClearDepBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamClearDepBuff buff;
		ArrayList<CFBamClearDepBuff> filteredList = new ArrayList<CFBamClearDepBuff>();
		CFBamClearDepBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamClearDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearDepBuff[0] ) );
	}

	public CFBamClearDepBuff[] readBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		CFBamClearDepBuff buff;
		ArrayList<CFBamClearDepBuff> filteredList = new ArrayList<CFBamClearDepBuff>();
		CFBamClearDepBuff[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearDepBuff[0] ) );
	}

	public CFBamClearDepBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		CFBamClearDepBuff buff;
		ArrayList<CFBamClearDepBuff> filteredList = new ArrayList<CFBamClearDepBuff>();
		CFBamClearDepBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearDepBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific ClearDep buffer instances identified by the duplicate key ClearDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The ClearDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearDepBuff[] pageBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearDep buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ClearDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearDepBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateClearDep( CFSecAuthorization Authorization,
		CFBamClearDepBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearDep",
				"Existing record not found",
				"ClearDep",
				pkey );
		}
		CFBamClearDepByClearDepIdxKey existingKeyClearDepIdx = schema.getFactoryClearDep().newClearDepIdxKey();
		existingKeyClearDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamClearDepByClearDepIdxKey newKeyClearDepIdx = schema.getFactoryClearDep().newClearDepIdxKey();
		newKeyClearDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamClearDepByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryClearDep().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamClearDepByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryClearDep().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearDep",
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
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearDep",
						"Lookup",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamClearDepBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearDepIdx.get( existingKeyClearDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearDepIdx.containsKey( newKeyClearDepIdx ) ) {
			subdict = dictByClearDepIdx.get( newKeyClearDepIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamClearDepBuff >();
			dictByClearDepIdx.put( newKeyClearDepIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamClearDepBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteClearDep( CFSecAuthorization Authorization,
		CFBamClearDepBuff Buff )
	{
		final String S_ProcName = "CFBamRamClearDepTable.deleteClearDep() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearDep",
				pkey );
		}
		CFBamClearDepByClearDepIdxKey keyClearDepIdx = schema.getFactoryClearDep().newClearDepIdxKey();
		keyClearDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamClearDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryClearDep().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"SuperClass",
				"ClearSubDep1",
				pkey );
		}

		if( schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"SuperClass",
				"ClearSubDep2",
				pkey );
		}

		if( schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"SuperClass",
				"ClearSubDep3",
				pkey );
		}

		if( schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"SuperClass",
				"ClearTopDep",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamClearDepBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearDepIdx.get( keyClearDepIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteClearDepByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearDepByClearDepIdx( Authorization, key );
	}

	public void deleteClearDepByClearDepIdx( CFSecAuthorization Authorization,
		CFBamClearDepByClearDepIdxKey argKey )
	{
		final String S_ProcName = "deleteClearDepByClearDepIdx";
		CFBamClearDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearDepBuff> matchSet = new LinkedList<CFBamClearDepBuff>();
		Iterator<CFBamClearDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a810".equals( subClassCode ) ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( "a811".equals( subClassCode ) ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (CFBamClearSubDep1Buff)cur );
			}
			else if( "a812".equals( subClassCode ) ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (CFBamClearSubDep2Buff)cur );
			}
			else if( "a813".equals( subClassCode ) ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (CFBamClearSubDep3Buff)cur );
			}
			else if( "a814".equals( subClassCode ) ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (CFBamClearTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ClearDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteClearDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearDepByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamClearDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteClearDepByDefSchemaIdx";
		CFBamClearDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearDepBuff> matchSet = new LinkedList<CFBamClearDepBuff>();
		Iterator<CFBamClearDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a810".equals( subClassCode ) ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( "a811".equals( subClassCode ) ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (CFBamClearSubDep1Buff)cur );
			}
			else if( "a812".equals( subClassCode ) ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (CFBamClearSubDep2Buff)cur );
			}
			else if( "a813".equals( subClassCode ) ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (CFBamClearSubDep3Buff)cur );
			}
			else if( "a814".equals( subClassCode ) ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (CFBamClearTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ClearDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteClearDepByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteClearDepByIdIdx( Authorization, key );
	}

	public void deleteClearDepByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		final String S_ProcName = "deleteClearDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamClearDepBuff cur;
		LinkedList<CFBamClearDepBuff> matchSet = new LinkedList<CFBamClearDepBuff>();
		Iterator<CFBamClearDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a810".equals( subClassCode ) ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( "a811".equals( subClassCode ) ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (CFBamClearSubDep1Buff)cur );
			}
			else if( "a812".equals( subClassCode ) ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (CFBamClearSubDep2Buff)cur );
			}
			else if( "a813".equals( subClassCode ) ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (CFBamClearSubDep3Buff)cur );
			}
			else if( "a814".equals( subClassCode ) ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (CFBamClearTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ClearDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteClearDepByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearDepByTenantIdx( Authorization, key );
	}

	public void deleteClearDepByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteClearDepByTenantIdx";
		CFBamClearDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearDepBuff> matchSet = new LinkedList<CFBamClearDepBuff>();
		Iterator<CFBamClearDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a810".equals( subClassCode ) ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( "a811".equals( subClassCode ) ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (CFBamClearSubDep1Buff)cur );
			}
			else if( "a812".equals( subClassCode ) ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (CFBamClearSubDep2Buff)cur );
			}
			else if( "a813".equals( subClassCode ) ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (CFBamClearSubDep3Buff)cur );
			}
			else if( "a814".equals( subClassCode ) ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (CFBamClearTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ClearDep must not be \"" + subClassCode + "\"" );
			}
		}
	}
}
