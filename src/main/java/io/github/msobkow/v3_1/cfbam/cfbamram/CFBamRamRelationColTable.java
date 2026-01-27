
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
 *	CFBamRamRelationColTable in-memory RAM DbIO implementation
 *	for RelationCol.
 */
public class CFBamRamRelationColTable
	implements ICFBamRelationColTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffRelationCol > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffRelationCol >();
	private Map< CFBamBuffRelationColByUNameIdxKey,
			CFBamBuffRelationCol > dictByUNameIdx
		= new HashMap< CFBamBuffRelationColByUNameIdxKey,
			CFBamBuffRelationCol >();
	private Map< CFBamBuffRelationColByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByRelationIdx
		= new HashMap< CFBamBuffRelationColByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffRelationColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByFromColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByFromColIdx
		= new HashMap< CFBamBuffRelationColByFromColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByToColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByToColIdx
		= new HashMap< CFBamBuffRelationColByToColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByPrevIdx
		= new HashMap< CFBamBuffRelationColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByNextIdx
		= new HashMap< CFBamBuffRelationColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByRelPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByRelPrevIdx
		= new HashMap< CFBamBuffRelationColByRelPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByRelNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByRelNextIdx
		= new HashMap< CFBamBuffRelationColByRelNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();

	public CFBamRamRelationColTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffRelationCol ensureRec(ICFBamRelationCol rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			if (classCode == ICFBamRelationCol.CLASS_CODE) {
				return( ((CFBamBuffRelationColDefaultFactory)(schema.getFactoryRelationCol())).ensureRec(rec) );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), "ensureRec", 1, "rec", "Not " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamRelationCol createRelationCol( ICFSecAuthorization Authorization,
		ICFBamRelationCol iBuff )
	{
		final String S_ProcName = "createRelationCol";
		
		CFBamBuffRelationCol Buff = ensureRec(iBuff);
			ICFBamRelationCol tail = null;

			ICFBamRelationCol[] siblings = schema.getTableRelationCol().readDerivedByRelationIdx( Authorization,
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
		
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextRelationColIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffRelationColByUNameIdxKey keyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRelationColByRelationIdxKey keyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffRelationColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRelationColByFromColIdxKey keyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		keyFromColIdx.setRequiredFromColId( Buff.getRequiredFromColId() );

		CFBamBuffRelationColByToColIdxKey keyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		keyToColIdx.setRequiredToColId( Buff.getRequiredToColId() );

		CFBamBuffRelationColByPrevIdxKey keyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByNextIdxKey keyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffRelationColByRelPrevIdxKey keyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		keyRelPrevIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyRelPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByRelNextIdxKey keyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelationIdx;
		if( dictByRelationIdx.containsKey( keyRelationIdx ) ) {
			subdictRelationIdx = dictByRelationIdx.get( keyRelationIdx );
		}
		else {
			subdictRelationIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelationIdx.put( keyRelationIdx, subdictRelationIdx );
		}
		subdictRelationIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictFromColIdx;
		if( dictByFromColIdx.containsKey( keyFromColIdx ) ) {
			subdictFromColIdx = dictByFromColIdx.get( keyFromColIdx );
		}
		else {
			subdictFromColIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByFromColIdx.put( keyFromColIdx, subdictFromColIdx );
		}
		subdictFromColIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictToColIdx;
		if( dictByToColIdx.containsKey( keyToColIdx ) ) {
			subdictToColIdx = dictByToColIdx.get( keyToColIdx );
		}
		else {
			subdictToColIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByToColIdx.put( keyToColIdx, subdictToColIdx );
		}
		subdictToColIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelPrevIdx;
		if( dictByRelPrevIdx.containsKey( keyRelPrevIdx ) ) {
			subdictRelPrevIdx = dictByRelPrevIdx.get( keyRelPrevIdx );
		}
		else {
			subdictRelPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelPrevIdx.put( keyRelPrevIdx, subdictRelPrevIdx );
		}
		subdictRelPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelNextIdx;
		if( dictByRelNextIdx.containsKey( keyRelNextIdx ) ) {
			subdictRelNextIdx = dictByRelNextIdx.get( keyRelNextIdx );
		}
		else {
			subdictRelNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelNextIdx.put( keyRelNextIdx, subdictRelNextIdx );
		}
		subdictRelNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamRelationCol tailEdit = schema.getFactoryRelationCol().newBuff();
			tailEdit.set( (ICFBamRelationCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableRelationCol().updateRelationCol( Authorization, tailEdit );
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamRelationCol.CLASS_CODE) {
				CFBamBuffRelationCol retbuff = ((CFBamBuffRelationCol)(schema.getFactoryRelationCol().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-create-buff-cloning-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamRelationCol readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerived";
		ICFBamRelationCol buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamRelationCol lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerived";
		ICFBamRelationCol buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamRelationCol[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamRelationCol.readAllDerived";
		ICFBamRelationCol[] retList = new ICFBamRelationCol[ dictByPKey.values().size() ];
		Iterator< ICFBamRelationCol > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamRelationCol readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByUNameIdx";
		CFBamBuffRelationColByUNameIdxKey key = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		key.setRequiredRelationId( RelationId );
		key.setRequiredName( Name );

		ICFBamRelationCol buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamRelationCol[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelationIdx";
		CFBamBuffRelationColByRelationIdxKey key = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		key.setRequiredRelationId( RelationId );

		ICFBamRelationCol[] recArray;
		if( dictByRelationIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelationIdx
				= dictByRelationIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictRelationIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictRelationIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelationIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelationIdx.put( key, subdictRelationIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByDefSchemaIdx";
		CFBamBuffRelationColByDefSchemaIdxKey key = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamRelationCol[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol[] readDerivedByFromColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByFromColIdx";
		CFBamBuffRelationColByFromColIdxKey key = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		key.setRequiredFromColId( FromColId );

		ICFBamRelationCol[] recArray;
		if( dictByFromColIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictFromColIdx
				= dictByFromColIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictFromColIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictFromColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictFromColIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByFromColIdx.put( key, subdictFromColIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol[] readDerivedByToColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByToColIdx";
		CFBamBuffRelationColByToColIdxKey key = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		key.setRequiredToColId( ToColId );

		ICFBamRelationCol[] recArray;
		if( dictByToColIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictToColIdx
				= dictByToColIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictToColIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictToColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictToColIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByToColIdx.put( key, subdictToColIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByPrevIdx";
		CFBamBuffRelationColByPrevIdxKey key = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		ICFBamRelationCol[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictPrevIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByNextIdx";
		CFBamBuffRelationColByNextIdxKey key = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		key.setOptionalNextId( NextId );

		ICFBamRelationCol[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictNextIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol[] readDerivedByRelPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelPrevIdx";
		CFBamBuffRelationColByRelPrevIdxKey key = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		key.setRequiredRelationId( RelationId );
		key.setOptionalPrevId( PrevId );

		ICFBamRelationCol[] recArray;
		if( dictByRelPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelPrevIdx
				= dictByRelPrevIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictRelPrevIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictRelPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelPrevIdx.put( key, subdictRelPrevIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol[] readDerivedByRelNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelNextIdx";
		CFBamBuffRelationColByRelNextIdxKey key = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		key.setRequiredRelationId( RelationId );
		key.setOptionalNextId( NextId );

		ICFBamRelationCol[] recArray;
		if( dictByRelNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelNextIdx
				= dictByRelNextIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictRelNextIdx.size() ];
			Iterator< ICFBamRelationCol > iter = subdictRelNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelNextIdx.put( key, subdictRelNextIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	public ICFBamRelationCol readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByIdIdx() ";
		ICFBamRelationCol buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamRelationCol readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuff";
		ICFBamRelationCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRelationCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamRelationCol lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamRelationCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRelationCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamRelationCol[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamRelationCol.readAllBuff";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByIdIdx() ";
		ICFBamRelationCol buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
			return( (ICFBamRelationCol)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamRelationCol readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByUNameIdx() ";
		ICFBamRelationCol buff = readDerivedByUNameIdx( Authorization,
			RelationId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
			return( (ICFBamRelationCol)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamRelationCol[] readBuffByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByRelationIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByDefSchemaIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol[] readBuffByFromColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByFromColIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByFromColIdx( Authorization,
			FromColId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol[] readBuffByToColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByToColIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByToColIdx( Authorization,
			ToColId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByPrevIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByNextIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol[] readBuffByRelPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByRelPrevIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByRelPrevIdx( Authorization,
			RelationId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	public ICFBamRelationCol[] readBuffByRelNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readBuffByRelNextIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByRelNextIdx( Authorization,
			RelationId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamRelationCol moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamRelationCol grandprev = null;
		ICFBamRelationCol prev = null;
		ICFBamRelationCol cur = null;
		ICFBamRelationCol next = null;

		cur = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamRelationColByIdIdxKey",
				"CFBamRelationColByIdIdxKey",
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
				"CFBamRelationColByIdIdxKey",
				"CFBamRelationColByIdIdxKey",
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamRelationColByIdIdxKey",
					"CFBamRelationColByIdIdxKey",
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamRelationColByIdIdxKey",
					"CFBamRelationColByIdIdxKey",
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		ICFBamRelationCol newInstance;
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		ICFBamRelationCol editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamRelationColBuff editCur = newInstance;
		editCur.set( cur );

		ICFBamRelationCol editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		ICFBamRelationCol editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
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
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-grand-prev-", "Not " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-prev-", "Not " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-cur-", "Not " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-next-", "Not " + Integer.toString(classCode));
			}
		}

		return( (CFBamRelationColBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamRelationCol moveBuffDown( ICFSecAuthorization Authorization,
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
				"CFBamRelationColByIdIdxKey",
				"CFBamRelationColByIdIdxKey",
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
				"CFBamRelationColByIdIdxKey",
				"CFBamRelationColByIdIdxKey",
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamRelationColByIdIdxKey",
					"CFBamRelationColByIdIdxKey",
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamRelationColByIdIdxKey",
					"CFBamRelationColByIdIdxKey",
					"Could not locate object.prev" );
			}
		}

		integer classCode = cur.getClassCode();
		CFBamRelationColBuff newInstance;
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamRelationColBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamRelationColBuff editNext = newInstance;
		editNext.set( next );

		CFBamRelationColBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamRelationColBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
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
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-prev-", "Not " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-cur-", "Not " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-next-", "Not " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-grand-next-", "Not " + Integer.toString(classCode));
			}
		}

		return( (CFBamRelationColBuff)editCur );
	}

	public ICFBamRelationCol updateRelationCol( ICFSecAuthorization Authorization,
		ICFBamRelationCol Buff )
	{
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamRelationCol existing = dictByPKey.get( pkey );
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
		CFBamBuffRelationColByUNameIdxKey existingKeyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRelationColByUNameIdxKey newKeyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRelationColByRelationIdxKey existingKeyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		existingKeyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffRelationColByRelationIdxKey newKeyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		newKeyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffRelationColByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRelationColByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRelationColByFromColIdxKey existingKeyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		existingKeyFromColIdx.setRequiredFromColId( existing.getRequiredFromColId() );

		CFBamBuffRelationColByFromColIdxKey newKeyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		newKeyFromColIdx.setRequiredFromColId( Buff.getRequiredFromColId() );

		CFBamBuffRelationColByToColIdxKey existingKeyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		existingKeyToColIdx.setRequiredToColId( existing.getRequiredToColId() );

		CFBamBuffRelationColByToColIdxKey newKeyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		newKeyToColIdx.setRequiredToColId( Buff.getRequiredToColId() );

		CFBamBuffRelationColByPrevIdxKey existingKeyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByPrevIdxKey newKeyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByNextIdxKey existingKeyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffRelationColByNextIdxKey newKeyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffRelationColByRelPrevIdxKey existingKeyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		existingKeyRelPrevIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyRelPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByRelPrevIdxKey newKeyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		newKeyRelPrevIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyRelPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByRelNextIdxKey existingKeyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		existingKeyRelNextIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyRelNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffRelationColByRelNextIdxKey newKeyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		newKeyRelNextIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyRelNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRelationCol",
					"RelationColUNameIdx",
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

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelNextIdx.put( newKeyRelNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	public void deleteRelationCol( ICFSecAuthorization Authorization,
		ICFBamRelationCol Buff )
	{
		final String S_ProcName = "CFBamRamRelationColTable.deleteRelationCol() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryRelationCol().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamRelationCol existing = dictByPKey.get( pkey );
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
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-update-prev-", "Not " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-edit-prev-", "Not " + Integer.toString(classCode));
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
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-update-next-", "Not " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a836" ) ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-edit-next-", "Not " + Integer.toString(classCode));
			}
		}

		CFBamBuffRelationColByUNameIdxKey keyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRelationColByRelationIdxKey keyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffRelationColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRelationColByFromColIdxKey keyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		keyFromColIdx.setRequiredFromColId( existing.getRequiredFromColId() );

		CFBamBuffRelationColByToColIdxKey keyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		keyToColIdx.setRequiredToColId( existing.getRequiredToColId() );

		CFBamBuffRelationColByPrevIdxKey keyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByNextIdxKey keyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffRelationColByRelPrevIdxKey keyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		keyRelPrevIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyRelPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByRelNextIdxKey keyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		keyRelNextIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyRelNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdict;

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
	public void deleteRelationColByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamRelationCol cur;
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		String argName )
	{
		CFBamBuffRelationColByUNameIdxKey key = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setRequiredName( argName );
		deleteRelationColByUNameIdx( Authorization, key );
	}

	public void deleteRelationColByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByUNameIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffRelationColByRelationIdxKey key = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteRelationColByRelationIdx( Authorization, key );
	}

	public void deleteRelationColByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByRelationIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffRelationColByDefSchemaIdxKey key = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteRelationColByDefSchemaIdx( Authorization, key );
	}

	public void deleteRelationColByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByDefSchemaIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByFromColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argFromColId )
	{
		CFBamBuffRelationColByFromColIdxKey key = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		key.setRequiredFromColId( argFromColId );
		deleteRelationColByFromColIdx( Authorization, key );
	}

	public void deleteRelationColByFromColIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByFromColIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByToColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argToColId )
	{
		CFBamBuffRelationColByToColIdxKey key = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		key.setRequiredToColId( argToColId );
		deleteRelationColByToColIdx( Authorization, key );
	}

	public void deleteRelationColByToColIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByToColIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffRelationColByPrevIdxKey key = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteRelationColByPrevIdx( Authorization, key );
	}

	public void deleteRelationColByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByPrevIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffRelationColByNextIdxKey key = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteRelationColByNextIdx( Authorization, key );
	}

	public void deleteRelationColByNextIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByNextIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByRelPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffRelationColByRelPrevIdxKey key = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setOptionalPrevId( argPrevId );
		deleteRelationColByRelPrevIdx( Authorization, key );
	}

	public void deleteRelationColByRelPrevIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByRelPrevIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}

	public void deleteRelationColByRelNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffRelationColByRelNextIdxKey key = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setOptionalNextId( argNextId );
		deleteRelationColByRelNextIdx( Authorization, key );
	}

	public void deleteRelationColByRelNextIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByRelNextIdxKey argKey )
	{
		ICFBamRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamRelationCol> matchSet = new LinkedList<ICFBamRelationCol>();
		Iterator<ICFBamRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelationCol( Authorization, cur );
		}
	}
}
