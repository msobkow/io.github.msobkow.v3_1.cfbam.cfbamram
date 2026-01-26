
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
 *	CFBamRamPopDepTable in-memory RAM DbIO implementation
 *	for PopDep.
 */
public class CFBamRamPopDepTable
	implements ICFBamPopDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffPopDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffPopDep >();
	private Map< CFBamBuffPopDepByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >> dictByRelationIdx
		= new HashMap< CFBamBuffPopDepByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >>();
	private Map< CFBamBuffPopDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffPopDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >>();

	public CFBamRamPopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createPopDep( ICFSecAuthorization Authorization,
		ICFBamPopDep Buff )
	{
		final String S_ProcName = "createPopDep";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamBuffPopDepByRelationIdxKey keyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffPopDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictRelationIdx;
		if( dictByRelationIdx.containsKey( keyRelationIdx ) ) {
			subdictRelationIdx = dictByRelationIdx.get( keyRelationIdx );
		}
		else {
			subdictRelationIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByRelationIdx.put( keyRelationIdx, subdictRelationIdx );
		}
		subdictRelationIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public ICFBamPopDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerived";
		ICFBamPopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamPopDep buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopDep.readAllDerived";
		ICFBamPopDep[] retList = new ICFBamPopDep[ dictByPKey.values().size() ];
		Iterator< ICFBamPopDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamPopDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopDep ) ) {
					filteredList.add( (ICFBamPopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopDep[0] ) );
		}
	}

	public ICFBamPopDep[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		CFBamBuffPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationId( RelationId );

		ICFBamPopDep[] recArray;
		if( dictByRelationIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictRelationIdx
				= dictByRelationIdx.get( key );
			recArray = new ICFBamPopDep[ subdictRelationIdx.size() ];
			Iterator< ICFBamPopDep > iter = subdictRelationIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictRelationIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByRelationIdx.put( key, subdictRelationIdx );
			recArray = new ICFBamPopDep[0];
		}
		return( recArray );
	}

	public ICFBamPopDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		CFBamBuffPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamPopDep[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamPopDep[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamPopDep > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamPopDep[0];
		}
		return( recArray );
	}

	public ICFBamPopDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		ICFBamPopDep buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopDep readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuff";
		ICFBamPopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a830" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopDep lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamPopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a830" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamPopDep[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopDep.readAllBuff";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
	}

	public ICFBamPopDep readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamPopDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (ICFBamPopDep)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamPopDep[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (ICFBamPopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
	}

	public ICFBamPopDep[] readBuffByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (ICFBamPopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
	}

	public ICFBamPopDep[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (ICFBamPopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
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
	public ICFBamPopDep[] pageBuffByRelationIdx( ICFSecAuthorization Authorization,
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
	public ICFBamPopDep[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updatePopDep( ICFSecAuthorization Authorization,
		ICFBamPopDep Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamPopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopDep",
				"Existing record not found",
				"PopDep",
				pkey );
		}
		CFBamBuffPopDepByRelationIdxKey existingKeyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		existingKeyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffPopDepByRelationIdxKey newKeyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		newKeyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffPopDepByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffPopDepByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deletePopDep( ICFSecAuthorization Authorization,
		ICFBamPopDep Buff )
	{
		final String S_ProcName = "CFBamRamPopDepTable.deletePopDep() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamPopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopDep",
				pkey );
		}
		CFBamBuffPopDepByRelationIdxKey keyRelationIdx = schema.getFactoryPopDep().newRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffPopDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryPopDep().newDefSchemaIdxKey();
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
		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRelationIdx.get( keyRelationIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deletePopDepByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopDepByRelationIdx( Authorization, key );
	}

	public void deletePopDepByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByRelationIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByRelationIdx";
		ICFBamPopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopDep> matchSet = new LinkedList<ICFBamPopDep>();
		Iterator<ICFBamPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
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

	public void deletePopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopDepByDefSchemaIdx( Authorization, key );
	}

	public void deletePopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByDefSchemaIdx";
		ICFBamPopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopDep> matchSet = new LinkedList<ICFBamPopDep>();
		Iterator<ICFBamPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
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

	public void deletePopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deletePopDepByIdIdx( Authorization, key );
	}

	public void deletePopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deletePopDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamPopDep cur;
		LinkedList<ICFBamPopDep> matchSet = new LinkedList<ICFBamPopDep>();
		Iterator<ICFBamPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
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

	public void deletePopDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopDepByTenantIdx( Authorization, key );
	}

	public void deletePopDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByTenantIdx";
		ICFBamPopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamPopDep> matchSet = new LinkedList<ICFBamPopDep>();
		Iterator<ICFBamPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
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
