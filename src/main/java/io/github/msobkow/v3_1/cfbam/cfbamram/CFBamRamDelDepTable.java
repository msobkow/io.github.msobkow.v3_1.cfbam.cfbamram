
// Description: Java 25 in-memory RAM DbIO implementation for DelDep.

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
 *	CFBamRamDelDepTable in-memory RAM DbIO implementation
 *	for DelDep.
 */
public class CFBamRamDelDepTable
	implements ICFBamDelDepTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamDelDepBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamDelDepBuff >();
	private Map< CFBamDelDepByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamDelDepBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamDelDepByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamDelDepBuff >>();
	private Map< CFBamDelDepByDelDepIdxKey,
				Map< CFBamScopePKey,
					CFBamDelDepBuff >> dictByDelDepIdx
		= new HashMap< CFBamDelDepByDelDepIdxKey,
				Map< CFBamScopePKey,
					CFBamDelDepBuff >>();

	public CFBamRamDelDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createDelDep( CFSecAuthorization Authorization,
		CFBamDelDepBuff Buff )
	{
		final String S_ProcName = "createDelDep";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamDelDepByDelDepIdxKey keyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		keyDelDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

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

		Map< CFBamScopePKey, CFBamDelDepBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamDelDepBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamDelDepBuff > subdictDelDepIdx;
		if( dictByDelDepIdx.containsKey( keyDelDepIdx ) ) {
			subdictDelDepIdx = dictByDelDepIdx.get( keyDelDepIdx );
		}
		else {
			subdictDelDepIdx = new HashMap< CFBamScopePKey, CFBamDelDepBuff >();
			dictByDelDepIdx.put( keyDelDepIdx, subdictDelDepIdx );
		}
		subdictDelDepIdx.put( pkey, Buff );

	}

	public CFBamDelDepBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelDepBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelDepBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelDep.readAllDerived";
		CFBamDelDepBuff[] retList = new CFBamDelDepBuff[ dictByPKey.values().size() ];
		Iterator< CFBamDelDepBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamDelDepBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamDelDepBuff> filteredList = new ArrayList<CFBamDelDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelDepBuff ) ) {
					filteredList.add( (CFBamDelDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelDepBuff[0] ) );
		}
	}

	public CFBamDelDepBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		CFBamDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamDelDepBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelDepBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamDelDepBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamDelDepBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelDepBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamDelDepBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamDelDepBuff[0];
		}
		return( recArray );
	}

	public CFBamDelDepBuff[] readDerivedByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		CFBamDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationId( RelationId );

		CFBamDelDepBuff[] recArray;
		if( dictByDelDepIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelDepBuff > subdictDelDepIdx
				= dictByDelDepIdx.get( key );
			recArray = new CFBamDelDepBuff[ subdictDelDepIdx.size() ];
			Iterator< CFBamDelDepBuff > iter = subdictDelDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelDepBuff > subdictDelDepIdx
				= new HashMap< CFBamScopePKey, CFBamDelDepBuff >();
			dictByDelDepIdx.put( key, subdictDelDepIdx );
			recArray = new CFBamDelDepBuff[0];
		}
		return( recArray );
	}

	public CFBamDelDepBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamDelDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelDepBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuff";
		CFBamDelDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a817" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelDepBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamDelDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a817" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelDepBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelDep.readAllBuff";
		CFBamDelDepBuff buff;
		ArrayList<CFBamDelDepBuff> filteredList = new ArrayList<CFBamDelDepBuff>();
		CFBamDelDepBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamDelDepBuff[0] ) );
	}

	public CFBamDelDepBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamDelDepBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamDelDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamDelDepBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamDelDepBuff buff;
		ArrayList<CFBamDelDepBuff> filteredList = new ArrayList<CFBamDelDepBuff>();
		CFBamDelDepBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamDelDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelDepBuff[0] ) );
	}

	public CFBamDelDepBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		CFBamDelDepBuff buff;
		ArrayList<CFBamDelDepBuff> filteredList = new ArrayList<CFBamDelDepBuff>();
		CFBamDelDepBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelDepBuff[0] ) );
	}

	public CFBamDelDepBuff[] readBuffByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		CFBamDelDepBuff buff;
		ArrayList<CFBamDelDepBuff> filteredList = new ArrayList<CFBamDelDepBuff>();
		CFBamDelDepBuff[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelDepBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific DelDep buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The DelDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelDepBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelDep buffer instances identified by the duplicate key DelDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The DelDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelDepBuff[] pageBuffByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDelDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateDelDep( CFSecAuthorization Authorization,
		CFBamDelDepBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelDep",
				"Existing record not found",
				"DelDep",
				pkey );
		}
		CFBamDelDepByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamDelDepByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamDelDepByDelDepIdxKey existingKeyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		existingKeyDelDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamDelDepByDelDepIdxKey newKeyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		newKeyDelDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelDep",
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
						"updateDelDep",
						"Lookup",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamDelDepBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamDelDepBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDelDepIdx.get( existingKeyDelDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelDepIdx.containsKey( newKeyDelDepIdx ) ) {
			subdict = dictByDelDepIdx.get( newKeyDelDepIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamDelDepBuff >();
			dictByDelDepIdx.put( newKeyDelDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteDelDep( CFSecAuthorization Authorization,
		CFBamDelDepBuff Buff )
	{
		final String S_ProcName = "CFBamRamDelDepTable.deleteDelDep() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelDep",
				pkey );
		}
		CFBamDelDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamDelDepByDelDepIdxKey keyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		keyDelDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		// Validate reverse foreign keys

		if( schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelSubDep1",
				pkey );
		}

		if( schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelSubDep2",
				pkey );
		}

		if( schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelSubDep3",
				pkey );
		}

		if( schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelTopDep",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamDelDepBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByDelDepIdx.get( keyDelDepIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteDelDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelDepByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByDefSchemaIdx";
		CFBamDelDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelDepBuff> matchSet = new LinkedList<CFBamDelDepBuff>();
		Iterator<CFBamDelDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (CFBamDelSubDep1Buff)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (CFBamDelSubDep2Buff)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (CFBamDelSubDep3Buff)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (CFBamDelTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteDelDepByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelDepByDelDepIdx( Authorization, key );
	}

	public void deleteDelDepByDelDepIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDelDepIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByDelDepIdx";
		CFBamDelDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelDepBuff> matchSet = new LinkedList<CFBamDelDepBuff>();
		Iterator<CFBamDelDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (CFBamDelSubDep1Buff)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (CFBamDelSubDep2Buff)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (CFBamDelSubDep3Buff)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (CFBamDelTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteDelDepByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteDelDepByIdIdx( Authorization, key );
	}

	public void deleteDelDepByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		final String S_ProcName = "deleteDelDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamDelDepBuff cur;
		LinkedList<CFBamDelDepBuff> matchSet = new LinkedList<CFBamDelDepBuff>();
		Iterator<CFBamDelDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (CFBamDelSubDep1Buff)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (CFBamDelSubDep2Buff)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (CFBamDelSubDep3Buff)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (CFBamDelTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteDelDepByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelDepByTenantIdx( Authorization, key );
	}

	public void deleteDelDepByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByTenantIdx";
		CFBamDelDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelDepBuff> matchSet = new LinkedList<CFBamDelDepBuff>();
		Iterator<CFBamDelDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (CFBamDelSubDep1Buff)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (CFBamDelSubDep2Buff)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (CFBamDelSubDep3Buff)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (CFBamDelTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}
}
