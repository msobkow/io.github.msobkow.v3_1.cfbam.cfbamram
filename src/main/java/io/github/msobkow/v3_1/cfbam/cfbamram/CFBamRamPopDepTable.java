
// Description: Java 25 in-memory RAM DbIO implementation for PopDep.

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
 *	CFBamRamPopDepTable in-memory RAM DbIO implementation
 *	for PopDep.
 */
public class CFBamRamPopDepTable
	implements ICFBamPopDepTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamPopDepBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamPopDepBuff >();
	private Map< CFBamPopDepByRelationIdxKey,
				Map< CFBamScopePKey,
					CFBamPopDepBuff >> dictByRelationIdx
		= new HashMap< CFBamPopDepByRelationIdxKey,
				Map< CFBamScopePKey,
					CFBamPopDepBuff >>();
	private Map< CFBamPopDepByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamPopDepBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamPopDepByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamPopDepBuff >>();

	public CFBamRamPopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createPopDep( CFSecAuthorization Authorization,
		CFBamPopDepBuff Buff )
	{
		final String S_ProcName = "createPopDep";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopDepByRelationIdxKey keyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamPopDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
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

		Map< CFBamScopePKey, CFBamPopDepBuff > subdictRelationIdx;
		if( dictByRelationIdx.containsKey( keyRelationIdx ) ) {
			subdictRelationIdx = dictByRelationIdx.get( keyRelationIdx );
		}
		else {
			subdictRelationIdx = new HashMap< CFBamScopePKey, CFBamPopDepBuff >();
			dictByRelationIdx.put( keyRelationIdx, subdictRelationIdx );
		}
		subdictRelationIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamPopDepBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamPopDepBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public CFBamPopDepBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopDepBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopDepBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopDep.readAllDerived";
		CFBamPopDepBuff[] retList = new CFBamPopDepBuff[ dictByPKey.values().size() ];
		Iterator< CFBamPopDepBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamPopDepBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopDepBuff> filteredList = new ArrayList<CFBamPopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopDepBuff ) ) {
					filteredList.add( (CFBamPopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopDepBuff[0] ) );
		}
	}

	public CFBamPopDepBuff[] readDerivedByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		CFBamPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationId( RelationId );

		CFBamPopDepBuff[] recArray;
		if( dictByRelationIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamPopDepBuff > subdictRelationIdx
				= dictByRelationIdx.get( key );
			recArray = new CFBamPopDepBuff[ subdictRelationIdx.size() ];
			Iterator< CFBamPopDepBuff > iter = subdictRelationIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamPopDepBuff > subdictRelationIdx
				= new HashMap< CFBamScopePKey, CFBamPopDepBuff >();
			dictByRelationIdx.put( key, subdictRelationIdx );
			recArray = new CFBamPopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamPopDepBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		CFBamPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamPopDepBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamPopDepBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamPopDepBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamPopDepBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamPopDepBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamPopDepBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamPopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamPopDepBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamPopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopDepBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuff";
		CFBamPopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a830" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopDepBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamPopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a830" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopDepBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopDep.readAllBuff";
		CFBamPopDepBuff buff;
		ArrayList<CFBamPopDepBuff> filteredList = new ArrayList<CFBamPopDepBuff>();
		CFBamPopDepBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamPopDepBuff[0] ) );
	}

	public CFBamPopDepBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamPopDepBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamPopDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamPopDepBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamPopDepBuff buff;
		ArrayList<CFBamPopDepBuff> filteredList = new ArrayList<CFBamPopDepBuff>();
		CFBamPopDepBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamPopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopDepBuff[0] ) );
	}

	public CFBamPopDepBuff[] readBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		CFBamPopDepBuff buff;
		ArrayList<CFBamPopDepBuff> filteredList = new ArrayList<CFBamPopDepBuff>();
		CFBamPopDepBuff[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopDepBuff[0] ) );
	}

	public CFBamPopDepBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		CFBamPopDepBuff buff;
		ArrayList<CFBamPopDepBuff> filteredList = new ArrayList<CFBamPopDepBuff>();
		CFBamPopDepBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopDepBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific PopDep buffer instances identified by the duplicate key RelationIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The PopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopDepBuff[] pageBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRelationIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopDep buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The PopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopDepBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updatePopDep( CFSecAuthorization Authorization,
		CFBamPopDepBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopDep",
				"Existing record not found",
				"PopDep",
				pkey );
		}
		CFBamPopDepByRelationIdxKey existingKeyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		existingKeyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamPopDepByRelationIdxKey newKeyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		newKeyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamPopDepByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamPopDepByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
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
						"updatePopDep",
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
						"updatePopDep",
						"Lookup",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamPopDepBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByRelationIdx.get( existingKeyRelationIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelationIdx.containsKey( newKeyRelationIdx ) ) {
			subdict = dictByRelationIdx.get( newKeyRelationIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamPopDepBuff >();
			dictByRelationIdx.put( newKeyRelationIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamPopDepBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deletePopDep( CFSecAuthorization Authorization,
		CFBamPopDepBuff Buff )
	{
		final String S_ProcName = "CFBamRamPopDepTable.deletePopDep() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopDep",
				pkey );
		}
		CFBamPopDepByRelationIdxKey keyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamPopDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"SuperClass",
				"PopSubDep1",
				pkey );
		}

		if( schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"SuperClass",
				"PopSubDep2",
				pkey );
		}

		if( schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"SuperClass",
				"PopSubDep3",
				pkey );
		}

		if( schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"SuperClass",
				"PopTopDep",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamPopDepBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRelationIdx.get( keyRelationIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deletePopDepByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopDepByRelationIdx( Authorization, key );
	}

	public void deletePopDepByRelationIdx( CFSecAuthorization Authorization,
		CFBamPopDepByRelationIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByRelationIdx";
		CFBamPopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopDepBuff> matchSet = new LinkedList<CFBamPopDepBuff>();
		Iterator<CFBamPopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (CFBamPopSubDep1Buff)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (CFBamPopSubDep2Buff)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (CFBamPopSubDep3Buff)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (CFBamPopTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of PopDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deletePopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopDepByDefSchemaIdx( Authorization, key );
	}

	public void deletePopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamPopDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByDefSchemaIdx";
		CFBamPopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopDepBuff> matchSet = new LinkedList<CFBamPopDepBuff>();
		Iterator<CFBamPopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (CFBamPopSubDep1Buff)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (CFBamPopSubDep2Buff)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (CFBamPopSubDep3Buff)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (CFBamPopTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of PopDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deletePopDepByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deletePopDepByIdIdx( Authorization, key );
	}

	public void deletePopDepByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		final String S_ProcName = "deletePopDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamPopDepBuff cur;
		LinkedList<CFBamPopDepBuff> matchSet = new LinkedList<CFBamPopDepBuff>();
		Iterator<CFBamPopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (CFBamPopSubDep1Buff)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (CFBamPopSubDep2Buff)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (CFBamPopSubDep3Buff)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (CFBamPopTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of PopDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deletePopDepByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopDepByTenantIdx( Authorization, key );
	}

	public void deletePopDepByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByTenantIdx";
		CFBamPopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopDepBuff> matchSet = new LinkedList<CFBamPopDepBuff>();
		Iterator<CFBamPopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (CFBamPopSubDep1Buff)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (CFBamPopSubDep2Buff)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (CFBamPopSubDep3Buff)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (CFBamPopTopDepBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of PopDep must not be \"" + subClassCode + "\"" );
			}
		}
	}
}
