
// Description: Java 25 in-memory RAM DbIO implementation for RelationCol.

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
 *	CFBamRamRelationColTable in-memory RAM DbIO implementation
 *	for RelationCol.
 */
public class CFBamRamRelationColTable
	implements ICFBamRelationColTable
{
	private ICFBamSchema schema;
	private Map< CFBamRelationColPKey,
				CFBamRelationColBuff > dictByPKey
		= new HashMap< CFBamRelationColPKey,
				CFBamRelationColBuff >();
	private Map< CFBamRelationColByUNameIdxKey,
			CFBamRelationColBuff > dictByUNameIdx
		= new HashMap< CFBamRelationColByUNameIdxKey,
			CFBamRelationColBuff >();
	private Map< CFBamRelationColByRelationIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByRelationIdx
		= new HashMap< CFBamRelationColByRelationIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();
	private Map< CFBamRelationColByDefSchemaIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamRelationColByDefSchemaIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();
	private Map< CFBamRelationColByFromColIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByFromColIdx
		= new HashMap< CFBamRelationColByFromColIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();
	private Map< CFBamRelationColByToColIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByToColIdx
		= new HashMap< CFBamRelationColByToColIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();
	private Map< CFBamRelationColByPrevIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByPrevIdx
		= new HashMap< CFBamRelationColByPrevIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();
	private Map< CFBamRelationColByNextIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByNextIdx
		= new HashMap< CFBamRelationColByNextIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();
	private Map< CFBamRelationColByRelPrevIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByRelPrevIdx
		= new HashMap< CFBamRelationColByRelPrevIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();
	private Map< CFBamRelationColByRelNextIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >> dictByRelNextIdx
		= new HashMap< CFBamRelationColByRelNextIdxKey,
				Map< CFBamRelationColPKey,
					CFBamRelationColBuff >>();

	public CFBamRamRelationColTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createRelationCol( CFSecAuthorization Authorization,
		CFBamRelationColBuff Buff )
	{
		final String S_ProcName = "createRelationCol";
			CFBamRelationColBuff tail = null;

			CFBamRelationColBuff[] siblings = schema.getTableRelationCol().readDerivedByRelationIdx( Authorization,
				Buff.getRequiredRelationId() );
			for( int idx = 0; ( tail == null ) && ( idx < siblings.length ); idx ++ ) {
				if( ( siblings[idx].getOptionalNextId() == null ) )
				{
					tail = siblings[idx];
				}
			}
			if( tail != null ) {
				Buff.setOptionalPrevId( tail.getRequiredId() );
			}
			else {
				Buff.setOptionalPrevId( null );
			}
		
		CFBamRelationColPKey pkey = schema.getFactoryRelationCol().newPKey();
		pkey.setRequiredId( schema.nextRelationColIdGen() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamRelationColByUNameIdxKey keyUNameIdx = schema.getFactoryRelationCol().newUNameIdxKey();
		keyUNameIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRelationColByRelationIdxKey keyRelationIdx = schema.getFactoryRelationCol().newRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamRelationColByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryRelationCol().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamRelationColByFromColIdxKey keyFromColIdx = schema.getFactoryRelationCol().newFromColIdxKey();
		keyFromColIdx.setRequiredFromColId( Buff.getRequiredFromColId() );

		CFBamRelationColByToColIdxKey keyToColIdx = schema.getFactoryRelationCol().newToColIdxKey();
		keyToColIdx.setRequiredToColId( Buff.getRequiredToColId() );

		CFBamRelationColByPrevIdxKey keyPrevIdx = schema.getFactoryRelationCol().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamRelationColByNextIdxKey keyNextIdx = schema.getFactoryRelationCol().newNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamRelationColByRelPrevIdxKey keyRelPrevIdx = schema.getFactoryRelationCol().newRelPrevIdxKey();
		keyRelPrevIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyRelPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamRelationColByRelNextIdxKey keyRelNextIdx = schema.getFactoryRelationCol().newRelNextIdxKey();
		keyRelNextIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyRelNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RelationColUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"LookupFromCol",
						"IndexCol",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"LookupToCol",
						"IndexCol",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelationIdx;
		if( dictByRelationIdx.containsKey( keyRelationIdx ) ) {
			subdictRelationIdx = dictByRelationIdx.get( keyRelationIdx );
		}
		else {
			subdictRelationIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelationIdx.put( keyRelationIdx, subdictRelationIdx );
		}
		subdictRelationIdx.put( pkey, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictFromColIdx;
		if( dictByFromColIdx.containsKey( keyFromColIdx ) ) {
			subdictFromColIdx = dictByFromColIdx.get( keyFromColIdx );
		}
		else {
			subdictFromColIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByFromColIdx.put( keyFromColIdx, subdictFromColIdx );
		}
		subdictFromColIdx.put( pkey, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictToColIdx;
		if( dictByToColIdx.containsKey( keyToColIdx ) ) {
			subdictToColIdx = dictByToColIdx.get( keyToColIdx );
		}
		else {
			subdictToColIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByToColIdx.put( keyToColIdx, subdictToColIdx );
		}
		subdictToColIdx.put( pkey, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelPrevIdx;
		if( dictByRelPrevIdx.containsKey( keyRelPrevIdx ) ) {
			subdictRelPrevIdx = dictByRelPrevIdx.get( keyRelPrevIdx );
		}
		else {
			subdictRelPrevIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelPrevIdx.put( keyRelPrevIdx, subdictRelPrevIdx );
		}
		subdictRelPrevIdx.put( pkey, Buff );

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelNextIdx;
		if( dictByRelNextIdx.containsKey( keyRelNextIdx ) ) {
			subdictRelNextIdx = dictByRelNextIdx.get( keyRelNextIdx );
		}
		else {
			subdictRelNextIdx = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelNextIdx.put( keyRelNextIdx, subdictRelNextIdx );
		}
		subdictRelNextIdx.put( pkey, Buff );

		if( tail != null ) {
			CFBamRelationColBuff tailEdit = schema.getFactoryRelationCol().newBuff();
			tailEdit.set( (CFBamRelationColBuff)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableRelationCol().updateRelationCol( Authorization, tailEdit );
		}
	}

	public CFBamRelationColBuff readDerived( CFSecAuthorization Authorization,
		CFBamRelationColPKey PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerived";
		CFBamRelationColPKey key = schema.getFactoryRelationCol().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamRelationColBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationColBuff lockDerived( CFSecAuthorization Authorization,
		CFBamRelationColPKey PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerived";
		CFBamRelationColPKey key = schema.getFactoryRelationCol().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamRelationColBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationColBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamRelationCol.readAllDerived";
		CFBamRelationColBuff[] retList = new CFBamRelationColBuff[ dictByPKey.values().size() ];
		Iterator< CFBamRelationColBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamRelationColBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByUNameIdx";
		CFBamRelationColByUNameIdxKey key = schema.getFactoryRelationCol().newUNameIdxKey();
		key.setRequiredRelationId( RelationId );
		key.setRequiredName( Name );

		CFBamRelationColBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationColBuff[] readDerivedByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelationIdx";
		CFBamRelationColByRelationIdxKey key = schema.getFactoryRelationCol().newRelationIdxKey();
		key.setRequiredRelationId( RelationId );

		CFBamRelationColBuff[] recArray;
		if( dictByRelationIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelationIdx
				= dictByRelationIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictRelationIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictRelationIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelationIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelationIdx.put( key, subdictRelationIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByDefSchemaIdx";
		CFBamRelationColByDefSchemaIdxKey key = schema.getFactoryRelationCol().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamRelationColBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictDefSchemaIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff[] readDerivedByFromColIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByFromColIdx";
		CFBamRelationColByFromColIdxKey key = schema.getFactoryRelationCol().newFromColIdxKey();
		key.setRequiredFromColId( FromColId );

		CFBamRelationColBuff[] recArray;
		if( dictByFromColIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictFromColIdx
				= dictByFromColIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictFromColIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictFromColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictFromColIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByFromColIdx.put( key, subdictFromColIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff[] readDerivedByToColIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByToColIdx";
		CFBamRelationColByToColIdxKey key = schema.getFactoryRelationCol().newToColIdxKey();
		key.setRequiredToColId( ToColId );

		CFBamRelationColBuff[] recArray;
		if( dictByToColIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictToColIdx
				= dictByToColIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictToColIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictToColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictToColIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByToColIdx.put( key, subdictToColIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByPrevIdx";
		CFBamRelationColByPrevIdxKey key = schema.getFactoryRelationCol().newPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		CFBamRelationColBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictPrevIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByNextIdx";
		CFBamRelationColByNextIdxKey key = schema.getFactoryRelationCol().newNextIdxKey();
		key.setOptionalNextId( NextId );

		CFBamRelationColBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictNextIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictNextIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff[] readDerivedByRelPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelPrevIdx";
		CFBamRelationColByRelPrevIdxKey key = schema.getFactoryRelationCol().newRelPrevIdxKey();
		key.setRequiredRelationId( RelationId );
		key.setOptionalPrevId( PrevId );

		CFBamRelationColBuff[] recArray;
		if( dictByRelPrevIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelPrevIdx
				= dictByRelPrevIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictRelPrevIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictRelPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelPrevIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelPrevIdx.put( key, subdictRelPrevIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff[] readDerivedByRelNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelNextIdx";
		CFBamRelationColByRelNextIdxKey key = schema.getFactoryRelationCol().newRelNextIdxKey();
		key.setRequiredRelationId( RelationId );
		key.setOptionalNextId( NextId );

		CFBamRelationColBuff[] recArray;
		if( dictByRelNextIdx.containsKey( key ) ) {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelNextIdx
				= dictByRelNextIdx.get( key );
			recArray = new CFBamRelationColBuff[ subdictRelNextIdx.size() ];
			Iterator< CFBamRelationColBuff > iter = subdictRelNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRelationColPKey, CFBamRelationColBuff > subdictRelNextIdx
				= new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelNextIdx.put( key, subdictRelNextIdx );
			recArray = new CFBamRelationColBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationColBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByIdIdx() ";
		CFBamRelationColPKey key = schema.getFactoryRelationCol().newPKey();
		key.setRequiredId( Id );

		CFBamRelationColBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationColBuff readBuff( CFSecAuthorization Authorization,
		CFBamRelationColPKey PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuff";
		CFBamRelationColBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a836" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationColBuff lockBuff( CFSecAuthorization Authorization,
		CFBamRelationColPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamRelationColBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a836" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationColBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamRelationCol.readAllBuff";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByIdIdx() ";
		CFBamRelationColBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
			return( (CFBamRelationColBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamRelationColBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByUNameIdx() ";
		CFBamRelationColBuff buff = readDerivedByUNameIdx( Authorization,
			RelationId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
			return( (CFBamRelationColBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamRelationColBuff[] readBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByRelationIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByDefSchemaIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff[] readBuffByFromColIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByFromColIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByFromColIdx( Authorization,
			FromColId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff[] readBuffByToColIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByToColIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByToColIdx( Authorization,
			ToColId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByPrevIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByNextIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff[] readBuffByRelPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByRelPrevIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByRelPrevIdx( Authorization,
			RelationId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	public CFBamRelationColBuff[] readBuffByRelNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByRelNextIdx() ";
		CFBamRelationColBuff buff;
		ArrayList<CFBamRelationColBuff> filteredList = new ArrayList<CFBamRelationColBuff>();
		CFBamRelationColBuff[] buffList = readDerivedByRelNextIdx( Authorization,
			RelationId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a836" ) ) {
				filteredList.add( (CFBamRelationColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationColBuff[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamRelationColBuff moveBuffUp( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamRelationColBuff grandprev = null;
		CFBamRelationColBuff prev = null;
		CFBamRelationColBuff cur = null;
		CFBamRelationColBuff next = null;

		cur = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamRelationColBuff)cur );
		}

		prev = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamRelationColBuff newInstance;
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamRelationColBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamRelationColBuff editCur = newInstance;
		editCur.set( cur );

		CFBamRelationColBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamRelationColBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext = newInstance;
			editNext.set( next );
		}

		if( editGrandprev != null ) {
			editGrandprev.setOptionalNextId( cur.getRequiredId() );
			editCur.setOptionalPrevId( grandprev.getRequiredId() );
		}
		else {
			editCur.setOptionalPrevId( null );
		}

			editPrev.setOptionalPrevId( cur.getRequiredId() );

			editCur.setOptionalNextId( prev.getRequiredId() );

		if( next != null ) {
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editPrev.setOptionalNextId( null );
		}

		if( editGrandprev != null ) {
			classCode = editGrandprev.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamRelationColBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamRelationColBuff moveBuffDown( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamRelationColBuff prev = null;
		CFBamRelationColBuff cur = null;
		CFBamRelationColBuff next = null;
		CFBamRelationColBuff grandnext = null;

		cur = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamRelationColBuff)cur );
		}

		next = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamRelationColBuff newInstance;
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamRelationColBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamRelationColBuff editNext = newInstance;
		editNext.set( next );

		CFBamRelationColBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamRelationColBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a836" ) ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev = newInstance;
			editPrev.set( prev );
		}

		if( prev != null ) {
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editNext.setOptionalPrevId( null );
		}

			editCur.setOptionalPrevId( next.getRequiredId() );

			editNext.setOptionalNextId( cur.getRequiredId() );

		if( editGrandnext != null ) {
			editCur.setOptionalNextId( grandnext.getRequiredId() );
			editGrandnext.setOptionalPrevId( cur.getRequiredId() );
		}
		else {
			editCur.setOptionalNextId( null );
		}

		if( editPrev != null ) {
			classCode = editPrev.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamRelationColBuff)editCur );
	}

	public void updateRelationCol( CFSecAuthorization Authorization,
		CFBamRelationColBuff Buff )
	{
		CFBamRelationColPKey pkey = schema.getFactoryRelationCol().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamRelationColBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateRelationCol",
				"Existing record not found",
				"RelationCol",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateRelationCol",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamRelationColByUNameIdxKey existingKeyUNameIdx = schema.getFactoryRelationCol().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamRelationColByUNameIdxKey newKeyUNameIdx = schema.getFactoryRelationCol().newUNameIdxKey();
		newKeyUNameIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRelationColByRelationIdxKey existingKeyRelationIdx = schema.getFactoryRelationCol().newRelationIdxKey();
		existingKeyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamRelationColByRelationIdxKey newKeyRelationIdx = schema.getFactoryRelationCol().newRelationIdxKey();
		newKeyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamRelationColByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryRelationCol().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamRelationColByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryRelationCol().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamRelationColByFromColIdxKey existingKeyFromColIdx = schema.getFactoryRelationCol().newFromColIdxKey();
		existingKeyFromColIdx.setRequiredFromColId( existing.getRequiredFromColId() );

		CFBamRelationColByFromColIdxKey newKeyFromColIdx = schema.getFactoryRelationCol().newFromColIdxKey();
		newKeyFromColIdx.setRequiredFromColId( Buff.getRequiredFromColId() );

		CFBamRelationColByToColIdxKey existingKeyToColIdx = schema.getFactoryRelationCol().newToColIdxKey();
		existingKeyToColIdx.setRequiredToColId( existing.getRequiredToColId() );

		CFBamRelationColByToColIdxKey newKeyToColIdx = schema.getFactoryRelationCol().newToColIdxKey();
		newKeyToColIdx.setRequiredToColId( Buff.getRequiredToColId() );

		CFBamRelationColByPrevIdxKey existingKeyPrevIdx = schema.getFactoryRelationCol().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamRelationColByPrevIdxKey newKeyPrevIdx = schema.getFactoryRelationCol().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamRelationColByNextIdxKey existingKeyNextIdx = schema.getFactoryRelationCol().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamRelationColByNextIdxKey newKeyNextIdx = schema.getFactoryRelationCol().newNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamRelationColByRelPrevIdxKey existingKeyRelPrevIdx = schema.getFactoryRelationCol().newRelPrevIdxKey();
		existingKeyRelPrevIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyRelPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamRelationColByRelPrevIdxKey newKeyRelPrevIdx = schema.getFactoryRelationCol().newRelPrevIdxKey();
		newKeyRelPrevIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyRelPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamRelationColByRelNextIdxKey existingKeyRelNextIdx = schema.getFactoryRelationCol().newRelNextIdxKey();
		existingKeyRelNextIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyRelNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamRelationColByRelNextIdxKey newKeyRelNextIdx = schema.getFactoryRelationCol().newRelNextIdxKey();
		newKeyRelNextIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyRelNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRelationCol",
					"RelationColUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelationCol",
						"Container",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelationCol",
						"Lookup",
						"LookupFromCol",
						"IndexCol",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelationCol",
						"Lookup",
						"LookupToCol",
						"IndexCol",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByRelationIdx.get( existingKeyRelationIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelationIdx.containsKey( newKeyRelationIdx ) ) {
			subdict = dictByRelationIdx.get( newKeyRelationIdx );
		}
		else {
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
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
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByFromColIdx.get( existingKeyFromColIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByFromColIdx.containsKey( newKeyFromColIdx ) ) {
			subdict = dictByFromColIdx.get( newKeyFromColIdx );
		}
		else {
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByFromColIdx.put( newKeyFromColIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByToColIdx.get( existingKeyToColIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByToColIdx.containsKey( newKeyToColIdx ) ) {
			subdict = dictByToColIdx.get( newKeyToColIdx );
		}
		else {
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByToColIdx.put( newKeyToColIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByPrevIdx.put( newKeyPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextIdx.get( existingKeyNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextIdx.containsKey( newKeyNextIdx ) ) {
			subdict = dictByNextIdx.get( newKeyNextIdx );
		}
		else {
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByRelPrevIdx.get( existingKeyRelPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelPrevIdx.containsKey( newKeyRelPrevIdx ) ) {
			subdict = dictByRelPrevIdx.get( newKeyRelPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelPrevIdx.put( newKeyRelPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByRelNextIdx.get( existingKeyRelNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelNextIdx.containsKey( newKeyRelNextIdx ) ) {
			subdict = dictByRelNextIdx.get( newKeyRelNextIdx );
		}
		else {
			subdict = new HashMap< CFBamRelationColPKey, CFBamRelationColBuff >();
			dictByRelNextIdx.put( newKeyRelNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteRelationCol( CFSecAuthorization Authorization,
		CFBamRelationColBuff Buff )
	{
		final String S_ProcName = "CFBamRamRelationColTable.deleteRelationCol() ";
		String classCode;
		CFBamRelationColPKey pkey = schema.getFactoryRelationCol().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamRelationColBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteRelationCol",
				pkey );
		}
		CFLibDbKeyHash256 varRelationId = existing.getRequiredRelationId();
		CFBamRelationBuff container = schema.getTableRelation().readDerivedByIdIdx( Authorization,
			varRelationId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamRelationColBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamRelationColBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a836" ) ) {
				editPrev = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamRelationColBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamRelationColBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a836" ) ) {
				editNext = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamRelationColByUNameIdxKey keyUNameIdx = schema.getFactoryRelationCol().newUNameIdxKey();
		keyUNameIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamRelationColByRelationIdxKey keyRelationIdx = schema.getFactoryRelationCol().newRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamRelationColByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryRelationCol().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamRelationColByFromColIdxKey keyFromColIdx = schema.getFactoryRelationCol().newFromColIdxKey();
		keyFromColIdx.setRequiredFromColId( existing.getRequiredFromColId() );

		CFBamRelationColByToColIdxKey keyToColIdx = schema.getFactoryRelationCol().newToColIdxKey();
		keyToColIdx.setRequiredToColId( existing.getRequiredToColId() );

		CFBamRelationColByPrevIdxKey keyPrevIdx = schema.getFactoryRelationCol().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamRelationColByNextIdxKey keyNextIdx = schema.getFactoryRelationCol().newNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamRelationColByRelPrevIdxKey keyRelPrevIdx = schema.getFactoryRelationCol().newRelPrevIdxKey();
		keyRelPrevIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyRelPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamRelationColByRelNextIdxKey keyRelNextIdx = schema.getFactoryRelationCol().newRelNextIdxKey();
		keyRelNextIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyRelNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamRelationColPKey, CFBamRelationColBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByRelationIdx.get( keyRelationIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByFromColIdx.get( keyFromColIdx );
		subdict.remove( pkey );

		subdict = dictByToColIdx.get( keyToColIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByRelPrevIdx.get( keyRelPrevIdx );
		subdict.remove( pkey );

		subdict = dictByRelNextIdx.get( keyRelNextIdx );
		subdict.remove( pkey );

	}
	public void deleteRelationColByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamRelationColPKey key = schema.getFactoryRelationCol().newPKey();
		key.setRequiredId( argId );
		deleteRelationColByIdIdx( Authorization, key );
	}

	public void deleteRelationColByIdIdx( CFSecAuthorization Authorization,
		CFBamRelationColPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamRelationColBuff cur;
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		String argName )
	{
		CFBamRelationColByUNameIdxKey key = schema.getFactoryRelationCol().newUNameIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setRequiredName( argName );
		deleteRelationColByUNameIdx( Authorization, key );
	}

	public void deleteRelationColByUNameIdx( CFSecAuthorization Authorization,
		CFBamRelationColByUNameIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamRelationColByRelationIdxKey key = schema.getFactoryRelationCol().newRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteRelationColByRelationIdx( Authorization, key );
	}

	public void deleteRelationColByRelationIdx( CFSecAuthorization Authorization,
		CFBamRelationColByRelationIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamRelationColByDefSchemaIdxKey key = schema.getFactoryRelationCol().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteRelationColByDefSchemaIdx( Authorization, key );
	}

	public void deleteRelationColByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamRelationColByDefSchemaIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByFromColIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argFromColId )
	{
		CFBamRelationColByFromColIdxKey key = schema.getFactoryRelationCol().newFromColIdxKey();
		key.setRequiredFromColId( argFromColId );
		deleteRelationColByFromColIdx( Authorization, key );
	}

	public void deleteRelationColByFromColIdx( CFSecAuthorization Authorization,
		CFBamRelationColByFromColIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByToColIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argToColId )
	{
		CFBamRelationColByToColIdxKey key = schema.getFactoryRelationCol().newToColIdxKey();
		key.setRequiredToColId( argToColId );
		deleteRelationColByToColIdx( Authorization, key );
	}

	public void deleteRelationColByToColIdx( CFSecAuthorization Authorization,
		CFBamRelationColByToColIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamRelationColByPrevIdxKey key = schema.getFactoryRelationCol().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteRelationColByPrevIdx( Authorization, key );
	}

	public void deleteRelationColByPrevIdx( CFSecAuthorization Authorization,
		CFBamRelationColByPrevIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamRelationColByNextIdxKey key = schema.getFactoryRelationCol().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteRelationColByNextIdx( Authorization, key );
	}

	public void deleteRelationColByNextIdx( CFSecAuthorization Authorization,
		CFBamRelationColByNextIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByRelPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamRelationColByRelPrevIdxKey key = schema.getFactoryRelationCol().newRelPrevIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setOptionalPrevId( argPrevId );
		deleteRelationColByRelPrevIdx( Authorization, key );
	}

	public void deleteRelationColByRelPrevIdx( CFSecAuthorization Authorization,
		CFBamRelationColByRelPrevIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByRelNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamRelationColByRelNextIdxKey key = schema.getFactoryRelationCol().newRelNextIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setOptionalNextId( argNextId );
		deleteRelationColByRelNextIdx( Authorization, key );
	}

	public void deleteRelationColByRelNextIdx( CFSecAuthorization Authorization,
		CFBamRelationColByRelNextIdxKey argKey )
	{
		CFBamRelationColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationColBuff> matchSet = new LinkedList<CFBamRelationColBuff>();
		Iterator<CFBamRelationColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}
}
