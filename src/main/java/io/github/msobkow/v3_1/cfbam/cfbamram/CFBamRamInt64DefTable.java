
// Description: Java 25 in-memory RAM DbIO implementation for Int64Def.

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
 *	CFBamRamInt64DefTable in-memory RAM DbIO implementation
 *	for Int64Def.
 */
public class CFBamRamInt64DefTable
	implements ICFBamInt64DefTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffInt64Def > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffInt64Def >();

	public CFBamRamInt64DefTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createInt64Def( ICFSecAuthorization Authorization,
		ICFBamInt64Def Buff )
	{
		final String S_ProcName = "createInt64Def";
		schema.getTableAtom().createAtom( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryValue().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableAtom().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"Atom",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

	}

	public ICFBamInt64Def readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamInt64Def.readDerived";
		ICFBamInt64Def buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamInt64Def lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamInt64Def.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryValue().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamInt64Def buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamInt64Def[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamInt64Def.readAllDerived";
		ICFBamInt64Def[] retList = new ICFBamInt64Def[ dictByPKey.values().size() ];
		Iterator< ICFBamInt64Def > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamInt64Def readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByUNameIdx";
		ICFBamValue buff = schema.getTableValue().readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamInt64Def ) {
			return( (ICFBamInt64Def)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamInt64Def[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByScopeIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			ScopeId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamInt64Def ) ) {
					filteredList.add( (ICFBamInt64Def)buff );
				}
			}
			return( filteredList.toArray( new ICFBamInt64Def[0] ) );
		}
	}

	public ICFBamInt64Def[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByDefSchemaIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamInt64Def ) ) {
					filteredList.add( (ICFBamInt64Def)buff );
				}
			}
			return( filteredList.toArray( new ICFBamInt64Def[0] ) );
		}
	}

	public ICFBamInt64Def[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByPrevIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByPrevIdx( Authorization,
			PrevId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamInt64Def ) ) {
					filteredList.add( (ICFBamInt64Def)buff );
				}
			}
			return( filteredList.toArray( new ICFBamInt64Def[0] ) );
		}
	}

	public ICFBamInt64Def[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByNextIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByNextIdx( Authorization,
			NextId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamInt64Def ) ) {
					filteredList.add( (ICFBamInt64Def)buff );
				}
			}
			return( filteredList.toArray( new ICFBamInt64Def[0] ) );
		}
	}

	public ICFBamInt64Def[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContPrevIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamInt64Def ) ) {
					filteredList.add( (ICFBamInt64Def)buff );
				}
			}
			return( filteredList.toArray( new ICFBamInt64Def[0] ) );
		}
	}

	public ICFBamInt64Def[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContNextIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamInt64Def ) ) {
					filteredList.add( (ICFBamInt64Def)buff );
				}
			}
			return( filteredList.toArray( new ICFBamInt64Def[0] ) );
		}
	}

	public ICFBamInt64Def readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryValue().newPKey();
		key.setRequiredId( Id );

		ICFBamInt64Def buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamInt64Def readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamInt64Def.readBuff";
		ICFBamInt64Def buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a827" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamInt64Def lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamInt64Def buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a827" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamInt64Def[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamInt64Def.readAllBuff";
		ICFBamInt64Def buff;
		ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
		ICFBamInt64Def[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a827" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamInt64Def[0] ) );
	}

	public ICFBamInt64Def readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByIdIdx() ";
		ICFBamInt64Def buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
			return( (ICFBamInt64Def)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamInt64Def readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByUNameIdx() ";
		ICFBamInt64Def buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
			return( (ICFBamInt64Def)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamInt64Def[] readBuffByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByScopeIdx() ";
		ICFBamInt64Def buff;
		ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
		ICFBamInt64Def[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamInt64Def)buff );
			}
		}
		return( filteredList.toArray( new ICFBamInt64Def[0] ) );
	}

	public ICFBamInt64Def[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByDefSchemaIdx() ";
		ICFBamInt64Def buff;
		ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
		ICFBamInt64Def[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamInt64Def)buff );
			}
		}
		return( filteredList.toArray( new ICFBamInt64Def[0] ) );
	}

	public ICFBamInt64Def[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByPrevIdx() ";
		ICFBamInt64Def buff;
		ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
		ICFBamInt64Def[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamInt64Def)buff );
			}
		}
		return( filteredList.toArray( new ICFBamInt64Def[0] ) );
	}

	public ICFBamInt64Def[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByNextIdx() ";
		ICFBamInt64Def buff;
		ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
		ICFBamInt64Def[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamInt64Def)buff );
			}
		}
		return( filteredList.toArray( new ICFBamInt64Def[0] ) );
	}

	public ICFBamInt64Def[] readBuffByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByContPrevIdx() ";
		ICFBamInt64Def buff;
		ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
		ICFBamInt64Def[] buffList = readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamInt64Def)buff );
			}
		}
		return( filteredList.toArray( new ICFBamInt64Def[0] ) );
	}

	public ICFBamInt64Def[] readBuffByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByContNextIdx() ";
		ICFBamInt64Def buff;
		ArrayList<ICFBamInt64Def> filteredList = new ArrayList<ICFBamInt64Def>();
		ICFBamInt64Def[] buffList = readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamInt64Def)buff );
			}
		}
		return( filteredList.toArray( new ICFBamInt64Def[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamInt64Def moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamValue grandprev = null;
		ICFBamValue prev = null;
		ICFBamValue cur = null;
		ICFBamValue next = null;

		cur = schema.getTableValue().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamInt64DefBuff)cur );
		}

		prev = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableValue().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		ICFBamValue newInstance;
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		ICFBamValue editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamValueBuff editCur = newInstance;
		editCur.set( cur );

		ICFBamValue editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		ICFBamValue editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
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
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editGrandprev );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editGrandprev );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editGrandprev );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editGrandprev );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editGrandprev );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editGrandprev );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editGrandprev );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editGrandprev );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editGrandprev );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editGrandprev );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editGrandprev );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editGrandprev );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editGrandprev );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editGrandprev );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editGrandprev );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editGrandprev );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editGrandprev );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editGrandprev );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editGrandprev );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editGrandprev );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editGrandprev );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editGrandprev );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editGrandprev );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editGrandprev );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editGrandprev );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editPrev );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editPrev );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editPrev );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editPrev );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editPrev );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editPrev );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editPrev );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editPrev );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editPrev );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editPrev );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editPrev );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editPrev );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editPrev );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editPrev );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editPrev );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editPrev );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editPrev );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editPrev );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editPrev );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editPrev );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editPrev );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editPrev );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editPrev );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editPrev );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editPrev );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editPrev );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editPrev );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editPrev );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editPrev );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editPrev );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editPrev );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editPrev );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editPrev );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editPrev );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editPrev );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editPrev );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editPrev );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editPrev );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editPrev );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editPrev );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editPrev );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editPrev );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editPrev );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editPrev );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editPrev );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editPrev );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editPrev );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editPrev );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editPrev );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editPrev );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editPrev );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editPrev );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editPrev );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editPrev );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editPrev );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editPrev );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editPrev );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editPrev );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editPrev );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editPrev );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editPrev );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editPrev );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editPrev );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editPrev );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editPrev );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editPrev );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editPrev );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editPrev );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editPrev );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editPrev );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editPrev );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editPrev );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editPrev );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editPrev );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editPrev );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editPrev );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editPrev );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editPrev );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editPrev );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editPrev );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editPrev );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editPrev );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editPrev );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editPrev );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editPrev );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editPrev );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editPrev );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editPrev );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editPrev );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editPrev );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editPrev );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editPrev );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editPrev );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editPrev );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editCur );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editCur );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editCur );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editCur );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editCur );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editCur );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editCur );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editCur );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editCur );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editCur );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editCur );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editCur );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editCur );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editCur );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editCur );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editCur );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editCur );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editCur );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editCur );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editCur );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editCur );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editCur );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editCur );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editCur );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editCur );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editCur );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editCur );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editCur );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editCur );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editCur );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editCur );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editCur );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editCur );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editCur );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editCur );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editCur );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editCur );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editCur );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editCur );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editCur );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editCur );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editCur );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editCur );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editCur );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editCur );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editCur );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editCur );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editCur );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editCur );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editCur );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editCur );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editCur );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editCur );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editCur );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editCur );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editCur );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editCur );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editCur );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editCur );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editCur );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editCur );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editCur );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editCur );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editCur );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editCur );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editCur );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editCur );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editCur );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editCur );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editCur );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editCur );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editCur );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editCur );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editCur );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editCur );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editCur );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editCur );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editCur );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editCur );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editCur );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editCur );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editCur );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editCur );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editCur );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editCur );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editCur );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editCur );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editCur );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editCur );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editCur );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editCur );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editCur );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editCur );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editCur );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editCur );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editCur );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editCur );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editCur );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editCur );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editCur );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editCur );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editCur );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editCur );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editCur );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editCur );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editNext );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editNext );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editNext );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editNext );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editNext );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editNext );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editNext );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editNext );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editNext );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editNext );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editNext );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editNext );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editNext );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editNext );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editNext );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editNext );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editNext );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editNext );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editNext );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editNext );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editNext );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editNext );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editNext );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editNext );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editNext );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editNext );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editNext );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editNext );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editNext );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editNext );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editNext );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editNext );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editNext );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editNext );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editNext );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editNext );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editNext );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editNext );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editNext );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editNext );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editNext );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editNext );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editNext );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editNext );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editNext );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editNext );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editNext );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editNext );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editNext );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editNext );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editNext );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editNext );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editNext );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editNext );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editNext );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editNext );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editNext );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editNext );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editNext );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editNext );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editNext );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editNext );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editNext );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editNext );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editNext );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editNext );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editNext );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editNext );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editNext );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editNext );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editNext );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editNext );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editNext );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editNext );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editNext );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editNext );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editNext );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editNext );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editNext );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editNext );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editNext );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editNext );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editNext );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editNext );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editNext );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editNext );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editNext );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editNext );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editNext );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editNext );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editNext );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editNext );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editNext );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editNext );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editNext );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editNext );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editNext );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editNext );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editNext );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editNext );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editNext );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editNext );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editNext );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editNext );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamInt64DefBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamInt64Def moveBuffDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamValueBuff prev = null;
		CFBamValueBuff cur = null;
		CFBamValueBuff next = null;
		CFBamValueBuff grandnext = null;

		cur = schema.getTableValue().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamInt64DefBuff)cur );
		}

		next = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableValue().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamValueBuff newInstance;
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamValueBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamValueBuff editNext = newInstance;
		editNext.set( next );

		CFBamValueBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamValueBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a809" ) ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				newInstance = schema.getFactoryTableCol().newBuff();
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
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editPrev );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editPrev );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editPrev );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editPrev );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editPrev );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editPrev );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editPrev );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editPrev );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editPrev );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editPrev );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editPrev );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editPrev );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editPrev );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editPrev );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editPrev );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editPrev );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editPrev );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editPrev );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editPrev );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editPrev );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editPrev );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editPrev );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editPrev );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editPrev );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editPrev );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editPrev );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editPrev );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editPrev );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editPrev );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editPrev );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editPrev );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editPrev );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editPrev );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editPrev );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editPrev );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editPrev );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editPrev );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editPrev );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editPrev );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editPrev );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editPrev );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editPrev );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editPrev );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editPrev );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editPrev );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editPrev );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editPrev );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editPrev );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editPrev );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editPrev );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editPrev );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editPrev );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editPrev );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editPrev );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editPrev );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editPrev );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editPrev );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editPrev );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editPrev );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editPrev );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editPrev );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editPrev );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editPrev );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editPrev );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editPrev );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editPrev );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editPrev );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editPrev );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editPrev );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editPrev );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editPrev );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editPrev );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editPrev );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editPrev );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editPrev );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editPrev );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editPrev );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editPrev );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editPrev );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editPrev );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editPrev );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editPrev );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editPrev );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editPrev );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editPrev );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editPrev );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editPrev );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editPrev );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editPrev );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editPrev );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editPrev );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editPrev );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editPrev );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editPrev );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editCur );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editCur );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editCur );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editCur );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editCur );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editCur );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editCur );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editCur );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editCur );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editCur );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editCur );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editCur );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editCur );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editCur );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editCur );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editCur );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editCur );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editCur );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editCur );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editCur );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editCur );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editCur );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editCur );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editCur );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editCur );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editCur );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editCur );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editCur );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editCur );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editCur );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editCur );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editCur );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editCur );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editCur );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editCur );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editCur );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editCur );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editCur );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editCur );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editCur );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editCur );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editCur );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editCur );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editCur );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editCur );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editCur );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editCur );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editCur );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editCur );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editCur );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editCur );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editCur );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editCur );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editCur );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editCur );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editCur );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editCur );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editCur );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editCur );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editCur );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editCur );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editCur );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editCur );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editCur );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editCur );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editCur );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editCur );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editCur );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editCur );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editCur );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editCur );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editCur );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editCur );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editCur );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editCur );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editCur );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editCur );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editCur );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editCur );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editCur );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editCur );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editCur );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editCur );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editCur );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editCur );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editCur );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editCur );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editCur );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editCur );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editCur );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editCur );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editCur );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editCur );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editCur );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editCur );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editCur );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editCur );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editCur );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editCur );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editCur );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editCur );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editCur );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editCur );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editCur );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editCur );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editNext );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editNext );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editNext );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editNext );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editNext );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editNext );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editNext );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editNext );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editNext );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editNext );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editNext );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editNext );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editNext );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editNext );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editNext );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editNext );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editNext );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editNext );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editNext );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editNext );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editNext );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editNext );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editNext );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editNext );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editNext );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editNext );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editNext );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editNext );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editNext );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editNext );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editNext );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editNext );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editNext );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editNext );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editNext );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editNext );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editNext );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editNext );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editNext );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editNext );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editNext );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editNext );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editNext );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editNext );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editNext );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editNext );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editNext );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editNext );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editNext );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editNext );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editNext );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editNext );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editNext );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editNext );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editNext );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editNext );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editNext );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editNext );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editNext );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editNext );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editNext );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editNext );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editNext );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editNext );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editNext );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editNext );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editNext );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editNext );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editNext );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editNext );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editNext );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editNext );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editNext );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editNext );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editNext );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editNext );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editNext );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editNext );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editNext );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editNext );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editNext );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editNext );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editNext );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editNext );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editNext );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editNext );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editNext );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editNext );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editNext );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editNext );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editNext );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editNext );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editNext );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editNext );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editNext );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editNext );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editNext );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editNext );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editNext );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editNext );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editNext );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editNext );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editNext );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editNext );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editGrandnext );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editGrandnext );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editGrandnext );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editGrandnext );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editGrandnext );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editGrandnext );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editGrandnext );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editGrandnext );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editGrandnext );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editGrandnext );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editGrandnext );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editGrandnext );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editGrandnext );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editGrandnext );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editGrandnext );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editGrandnext );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editGrandnext );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editGrandnext );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editGrandnext );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editGrandnext );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editGrandnext );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editGrandnext );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editGrandnext );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editGrandnext );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editGrandnext );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamInt64DefBuff)editCur );
	}

	public void updateInt64Def( ICFSecAuthorization Authorization,
		ICFBamInt64Def Buff )
	{
		schema.getTableAtom().updateAtom( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryValue().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamInt64Def existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateInt64Def",
				"Existing record not found",
				"Int64Def",
				pkey );
		}
		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableAtom().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateInt64Def",
						"Superclass",
						"SuperClass",
						"Atom",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffInt64Def > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

	}

	public void deleteInt64Def( ICFSecAuthorization Authorization,
		ICFBamInt64Def Buff )
	{
		final String S_ProcName = "CFBamRamInt64DefTable.deleteInt64Def() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryValue().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamInt64Def existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteInt64Def",
				pkey );
		}
		CFLibDbKeyHash256 varScopeId = existing.getRequiredScopeId();
		CFBamScopeBuff container = schema.getTableScope().readDerivedByIdIdx( Authorization,
			varScopeId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamValueBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableValue().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamValueBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a809" ) ) {
				editPrev = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				editPrev = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				editPrev = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				editPrev = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				editPrev = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				editPrev = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				editPrev = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				editPrev = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				editPrev = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				editPrev = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				editPrev = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				editPrev = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				editPrev = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				editPrev = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				editPrev = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				editPrev = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				editPrev = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				editPrev = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				editPrev = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				editPrev = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				editPrev = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				editPrev = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				editPrev = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				editPrev = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				editPrev = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				editPrev = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				editPrev = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				editPrev = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				editPrev = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				editPrev = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				editPrev = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				editPrev = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				editPrev = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				editPrev = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				editPrev = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				editPrev = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				editPrev = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				editPrev = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				editPrev = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				editPrev = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				editPrev = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				editPrev = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				editPrev = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				editPrev = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				editPrev = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				editPrev = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				editPrev = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				editPrev = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				editPrev = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				editPrev = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				editPrev = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				editPrev = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				editPrev = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				editPrev = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				editPrev = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				editPrev = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				editPrev = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				editPrev = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				editPrev = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				editPrev = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				editPrev = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				editPrev = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				editPrev = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				editPrev = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				editPrev = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				editPrev = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				editPrev = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				editPrev = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				editPrev = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				editPrev = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				editPrev = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				editPrev = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				editPrev = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				editPrev = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				editPrev = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				editPrev = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				editPrev = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				editPrev = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				editPrev = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				editPrev = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				editPrev = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				editPrev = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				editPrev = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				editPrev = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				editPrev = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				editPrev = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				editPrev = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				editPrev = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				editPrev = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				editPrev = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				editPrev = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				editPrev = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				editPrev = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				editPrev = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				editPrev = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				editPrev = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				editPrev = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				editPrev = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				editPrev = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				editPrev = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				editPrev = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				editPrev = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				editPrev = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				editPrev = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				editPrev = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				editPrev = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editPrev );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editPrev );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editPrev );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editPrev );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editPrev );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editPrev );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editPrev );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editPrev );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editPrev );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editPrev );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editPrev );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editPrev );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editPrev );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editPrev );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editPrev );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editPrev );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editPrev );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editPrev );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editPrev );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editPrev );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editPrev );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editPrev );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editPrev );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editPrev );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editPrev );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editPrev );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editPrev );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editPrev );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editPrev );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editPrev );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editPrev );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editPrev );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editPrev );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editPrev );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editPrev );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editPrev );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editPrev );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editPrev );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editPrev );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editPrev );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editPrev );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editPrev );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editPrev );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editPrev );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editPrev );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editPrev );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editPrev );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editPrev );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editPrev );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editPrev );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editPrev );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editPrev );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editPrev );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editPrev );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editPrev );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editPrev );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editPrev );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editPrev );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editPrev );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editPrev );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editPrev );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editPrev );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editPrev );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editPrev );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editPrev );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editPrev );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editPrev );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editPrev );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editPrev );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editPrev );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editPrev );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editPrev );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editPrev );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editPrev );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editPrev );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editPrev );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editPrev );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editPrev );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editPrev );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editPrev );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editPrev );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editPrev );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editPrev );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editPrev );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editPrev );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editPrev );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editPrev );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editPrev );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editPrev );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editPrev );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editPrev );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editPrev );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editPrev );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editPrev );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editPrev );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editPrev );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamValueBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableValue().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamValueBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a809" ) ) {
				editNext = schema.getFactoryValue().newBuff();
			}
			else if( classCode.equals( "a80a" ) ) {
				editNext = schema.getFactoryAtom().newBuff();
			}
			else if( classCode.equals( "a80b" ) ) {
				editNext = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode.equals( "a80c" ) ) {
				editNext = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode.equals( "a86b" ) ) {
				editNext = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode.equals( "a80d" ) ) {
				editNext = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode.equals( "a80e" ) ) {
				editNext = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode.equals( "a86c" ) ) {
				editNext = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode.equals( "a815" ) ) {
				editNext = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode.equals( "a816" ) ) {
				editNext = schema.getFactoryDateType().newBuff();
			}
			else if( classCode.equals( "a86d" ) ) {
				editNext = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode.equals( "a81c" ) ) {
				editNext = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode.equals( "a81d" ) ) {
				editNext = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode.equals( "a86e" ) ) {
				editNext = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode.equals( "a81f" ) ) {
				editNext = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode.equals( "a820" ) ) {
				editNext = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode.equals( "a871" ) ) {
				editNext = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode.equals( "a823" ) ) {
				editNext = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode.equals( "a824" ) ) {
				editNext = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode.equals( "a872" ) ) {
				editNext = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode.equals( "a86f" ) ) {
				editNext = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode.equals( "a870" ) ) {
				editNext = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode.equals( "a875" ) ) {
				editNext = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode.equals( "a825" ) ) {
				editNext = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode.equals( "a826" ) ) {
				editNext = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode.equals( "a873" ) ) {
				editNext = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode.equals( "a876" ) ) {
				editNext = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode.equals( "a827" ) ) {
				editNext = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode.equals( "a828" ) ) {
				editNext = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode.equals( "a874" ) ) {
				editNext = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode.equals( "a877" ) ) {
				editNext = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode.equals( "a829" ) ) {
				editNext = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode.equals( "a82a" ) ) {
				editNext = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode.equals( "a878" ) ) {
				editNext = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode.equals( "a82b" ) ) {
				editNext = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode.equals( "a82c" ) ) {
				editNext = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode.equals( "a879" ) ) {
				editNext = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode.equals( "a82d" ) ) {
				editNext = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode.equals( "a82e" ) ) {
				editNext = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode.equals( "a87a" ) ) {
				editNext = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode.equals( "a839" ) ) {
				editNext = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode.equals( "a838" ) ) {
				editNext = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode.equals( "a83a" ) ) {
				editNext = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode.equals( "a83b" ) ) {
				editNext = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode.equals( "a83d" ) ) {
				editNext = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode.equals( "a83c" ) ) {
				editNext = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode.equals( "a83e" ) ) {
				editNext = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode.equals( "a83f" ) ) {
				editNext = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode.equals( "a841" ) ) {
				editNext = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode.equals( "a840" ) ) {
				editNext = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode.equals( "a842" ) ) {
				editNext = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode.equals( "a843" ) ) {
				editNext = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode.equals( "a845" ) ) {
				editNext = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode.equals( "a844" ) ) {
				editNext = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode.equals( "a846" ) ) {
				editNext = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode.equals( "a847" ) ) {
				editNext = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode.equals( "a849" ) ) {
				editNext = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode.equals( "a848" ) ) {
				editNext = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode.equals( "a84a" ) ) {
				editNext = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode.equals( "a84b" ) ) {
				editNext = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode.equals( "a84d" ) ) {
				editNext = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode.equals( "a84c" ) ) {
				editNext = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode.equals( "a84e" ) ) {
				editNext = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode.equals( "a84f" ) ) {
				editNext = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode.equals( "a850" ) ) {
				editNext = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode.equals( "a851" ) ) {
				editNext = schema.getFactoryStringType().newBuff();
			}
			else if( classCode.equals( "a87b" ) ) {
				editNext = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode.equals( "a852" ) ) {
				editNext = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode.equals( "a853" ) ) {
				editNext = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode.equals( "a87c" ) ) {
				editNext = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode.equals( "a854" ) ) {
				editNext = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode.equals( "a855" ) ) {
				editNext = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode.equals( "a87d" ) ) {
				editNext = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode.equals( "a856" ) ) {
				editNext = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode.equals( "a857" ) ) {
				editNext = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode.equals( "a87e" ) ) {
				editNext = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode.equals( "a859" ) ) {
				editNext = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode.equals( "a85a" ) ) {
				editNext = schema.getFactoryTextType().newBuff();
			}
			else if( classCode.equals( "a87f" ) ) {
				editNext = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode.equals( "a85b" ) ) {
				editNext = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode.equals( "a85c" ) ) {
				editNext = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode.equals( "a880" ) ) {
				editNext = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode.equals( "a85d" ) ) {
				editNext = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode.equals( "a85e" ) ) {
				editNext = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode.equals( "a881" ) ) {
				editNext = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode.equals( "a85f" ) ) {
				editNext = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode.equals( "a860" ) ) {
				editNext = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode.equals( "a882" ) ) {
				editNext = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode.equals( "a861" ) ) {
				editNext = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode.equals( "a862" ) ) {
				editNext = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode.equals( "a883" ) ) {
				editNext = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode.equals( "a863" ) ) {
				editNext = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode.equals( "a864" ) ) {
				editNext = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode.equals( "a884" ) ) {
				editNext = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode.equals( "a865" ) ) {
				editNext = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode.equals( "a866" ) ) {
				editNext = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode.equals( "a885" ) ) {
				editNext = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode.equals( "a867" ) ) {
				editNext = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode.equals( "a869" ) ) {
				editNext = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode.equals( "a888" ) ) {
				editNext = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode.equals( "a886" ) ) {
				editNext = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode.equals( "a868" ) ) {
				editNext = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode.equals( "a86a" ) ) {
				editNext = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode.equals( "a889" ) ) {
				editNext = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode.equals( "a887" ) ) {
				editNext = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode.equals( "a858" ) ) {
				editNext = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode.equals( "a80a" ) ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editNext );
			}
			else if( classCode.equals( "a80b" ) ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editNext );
			}
			else if( classCode.equals( "a80c" ) ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editNext );
			}
			else if( classCode.equals( "a86b" ) ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editNext );
			}
			else if( classCode.equals( "a80d" ) ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editNext );
			}
			else if( classCode.equals( "a80e" ) ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editNext );
			}
			else if( classCode.equals( "a86c" ) ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editNext );
			}
			else if( classCode.equals( "a815" ) ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editNext );
			}
			else if( classCode.equals( "a816" ) ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editNext );
			}
			else if( classCode.equals( "a86d" ) ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editNext );
			}
			else if( classCode.equals( "a81c" ) ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editNext );
			}
			else if( classCode.equals( "a81d" ) ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editNext );
			}
			else if( classCode.equals( "a86e" ) ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editNext );
			}
			else if( classCode.equals( "a81f" ) ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editNext );
			}
			else if( classCode.equals( "a820" ) ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editNext );
			}
			else if( classCode.equals( "a871" ) ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editNext );
			}
			else if( classCode.equals( "a823" ) ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editNext );
			}
			else if( classCode.equals( "a824" ) ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editNext );
			}
			else if( classCode.equals( "a872" ) ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editNext );
			}
			else if( classCode.equals( "a86f" ) ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editNext );
			}
			else if( classCode.equals( "a870" ) ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editNext );
			}
			else if( classCode.equals( "a875" ) ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editNext );
			}
			else if( classCode.equals( "a825" ) ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editNext );
			}
			else if( classCode.equals( "a826" ) ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editNext );
			}
			else if( classCode.equals( "a873" ) ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editNext );
			}
			else if( classCode.equals( "a876" ) ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editNext );
			}
			else if( classCode.equals( "a827" ) ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editNext );
			}
			else if( classCode.equals( "a828" ) ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editNext );
			}
			else if( classCode.equals( "a874" ) ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editNext );
			}
			else if( classCode.equals( "a877" ) ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editNext );
			}
			else if( classCode.equals( "a829" ) ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editNext );
			}
			else if( classCode.equals( "a82a" ) ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editNext );
			}
			else if( classCode.equals( "a878" ) ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editNext );
			}
			else if( classCode.equals( "a82b" ) ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editNext );
			}
			else if( classCode.equals( "a82c" ) ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editNext );
			}
			else if( classCode.equals( "a879" ) ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editNext );
			}
			else if( classCode.equals( "a82d" ) ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editNext );
			}
			else if( classCode.equals( "a82e" ) ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editNext );
			}
			else if( classCode.equals( "a87a" ) ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editNext );
			}
			else if( classCode.equals( "a839" ) ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editNext );
			}
			else if( classCode.equals( "a838" ) ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editNext );
			}
			else if( classCode.equals( "a83a" ) ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editNext );
			}
			else if( classCode.equals( "a83b" ) ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editNext );
			}
			else if( classCode.equals( "a83d" ) ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editNext );
			}
			else if( classCode.equals( "a83c" ) ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editNext );
			}
			else if( classCode.equals( "a83e" ) ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editNext );
			}
			else if( classCode.equals( "a83f" ) ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editNext );
			}
			else if( classCode.equals( "a841" ) ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editNext );
			}
			else if( classCode.equals( "a840" ) ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editNext );
			}
			else if( classCode.equals( "a842" ) ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editNext );
			}
			else if( classCode.equals( "a843" ) ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editNext );
			}
			else if( classCode.equals( "a845" ) ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editNext );
			}
			else if( classCode.equals( "a844" ) ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editNext );
			}
			else if( classCode.equals( "a846" ) ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editNext );
			}
			else if( classCode.equals( "a847" ) ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editNext );
			}
			else if( classCode.equals( "a849" ) ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editNext );
			}
			else if( classCode.equals( "a848" ) ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editNext );
			}
			else if( classCode.equals( "a84a" ) ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editNext );
			}
			else if( classCode.equals( "a84b" ) ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editNext );
			}
			else if( classCode.equals( "a84d" ) ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editNext );
			}
			else if( classCode.equals( "a84c" ) ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editNext );
			}
			else if( classCode.equals( "a84e" ) ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editNext );
			}
			else if( classCode.equals( "a84f" ) ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editNext );
			}
			else if( classCode.equals( "a850" ) ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editNext );
			}
			else if( classCode.equals( "a851" ) ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editNext );
			}
			else if( classCode.equals( "a87b" ) ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editNext );
			}
			else if( classCode.equals( "a852" ) ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editNext );
			}
			else if( classCode.equals( "a853" ) ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editNext );
			}
			else if( classCode.equals( "a87c" ) ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editNext );
			}
			else if( classCode.equals( "a854" ) ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editNext );
			}
			else if( classCode.equals( "a855" ) ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editNext );
			}
			else if( classCode.equals( "a87d" ) ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editNext );
			}
			else if( classCode.equals( "a856" ) ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editNext );
			}
			else if( classCode.equals( "a857" ) ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editNext );
			}
			else if( classCode.equals( "a87e" ) ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editNext );
			}
			else if( classCode.equals( "a859" ) ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editNext );
			}
			else if( classCode.equals( "a85a" ) ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editNext );
			}
			else if( classCode.equals( "a87f" ) ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editNext );
			}
			else if( classCode.equals( "a85b" ) ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editNext );
			}
			else if( classCode.equals( "a85c" ) ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editNext );
			}
			else if( classCode.equals( "a880" ) ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editNext );
			}
			else if( classCode.equals( "a85d" ) ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editNext );
			}
			else if( classCode.equals( "a85e" ) ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editNext );
			}
			else if( classCode.equals( "a881" ) ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editNext );
			}
			else if( classCode.equals( "a85f" ) ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editNext );
			}
			else if( classCode.equals( "a860" ) ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editNext );
			}
			else if( classCode.equals( "a882" ) ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editNext );
			}
			else if( classCode.equals( "a861" ) ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editNext );
			}
			else if( classCode.equals( "a862" ) ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editNext );
			}
			else if( classCode.equals( "a883" ) ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editNext );
			}
			else if( classCode.equals( "a863" ) ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editNext );
			}
			else if( classCode.equals( "a864" ) ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editNext );
			}
			else if( classCode.equals( "a884" ) ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editNext );
			}
			else if( classCode.equals( "a865" ) ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editNext );
			}
			else if( classCode.equals( "a866" ) ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editNext );
			}
			else if( classCode.equals( "a885" ) ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editNext );
			}
			else if( classCode.equals( "a867" ) ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editNext );
			}
			else if( classCode.equals( "a869" ) ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editNext );
			}
			else if( classCode.equals( "a888" ) ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editNext );
			}
			else if( classCode.equals( "a886" ) ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editNext );
			}
			else if( classCode.equals( "a868" ) ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editNext );
			}
			else if( classCode.equals( "a86a" ) ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editNext );
			}
			else if( classCode.equals( "a889" ) ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editNext );
			}
			else if( classCode.equals( "a887" ) ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editNext );
			}
			else if( classCode.equals( "a858" ) ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckReferencingTableCols[] = schema.getTableTableCol().readDerivedByDataIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckReferencingTableCols.length > 0 ) {
			schema.getTableTableCol().deleteTableColByDataIdx( Authorization,
						existing.getRequiredId() );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckReferencingIndexCols[] = schema.getTableIndexCol().readDerivedByColIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckReferencingIndexCols.length > 0 ) {
			schema.getTableIndexCol().deleteIndexColByColIdx( Authorization,
						existing.getRequiredId() );
		}
		// Validate reverse foreign keys

		if( schema.getTableInt64Type().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteInt64Def",
				"Superclass",
				"SuperClass",
				"Int64Type",
				pkey );
		}

		if( schema.getTableInt64Col().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteInt64Def",
				"Superclass",
				"SuperClass",
				"Int64Col",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffInt64Def > subdict;

		dictByPKey.remove( pkey );

		schema.getTableAtom().deleteAtom( Authorization,
			Buff );
	}
	public void deleteInt64DefByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryValue().newPKey();
		key.setRequiredId( argId );
		deleteInt64DefByIdIdx( Authorization, key );
	}

	public void deleteInt64DefByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteInt64DefByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamInt64Def cur;
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteInt64DefByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffValueByUNameIdxKey key = schema.getFactoryValue().newUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteInt64DefByUNameIdx( Authorization, key );
	}

	public void deleteInt64DefByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamValueByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteInt64DefByUNameIdx";
		ICFBamInt64Def cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteInt64DefByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffValueByScopeIdxKey key = schema.getFactoryValue().newScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteInt64DefByScopeIdx( Authorization, key );
	}

	public void deleteInt64DefByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamValueByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteInt64DefByScopeIdx";
		ICFBamInt64Def cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteInt64DefByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffValueByDefSchemaIdxKey key = schema.getFactoryValue().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteInt64DefByDefSchemaIdx( Authorization, key );
	}

	public void deleteInt64DefByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamValueByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteInt64DefByDefSchemaIdx";
		ICFBamInt64Def cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteInt64DefByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByPrevIdxKey key = schema.getFactoryValue().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteInt64DefByPrevIdx( Authorization, key );
	}

	public void deleteInt64DefByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteInt64DefByPrevIdx";
		ICFBamInt64Def cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteInt64DefByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByNextIdxKey key = schema.getFactoryValue().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteInt64DefByNextIdx( Authorization, key );
	}

	public void deleteInt64DefByNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByNextIdxKey argKey )
	{
		final String S_ProcName = "deleteInt64DefByNextIdx";
		ICFBamInt64Def cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteInt64DefByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByContPrevIdxKey key = schema.getFactoryValue().newContPrevIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalPrevId( argPrevId );
		deleteInt64DefByContPrevIdx( Authorization, key );
	}

	public void deleteInt64DefByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteInt64DefByContPrevIdx";
		ICFBamInt64Def cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteInt64DefByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByContNextIdxKey key = schema.getFactoryValue().newContNextIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalNextId( argNextId );
		deleteInt64DefByContNextIdx( Authorization, key );
	}

	public void deleteInt64DefByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContNextIdxKey argKey )
	{
		final String S_ProcName = "deleteInt64DefByContNextIdx";
		ICFBamInt64Def cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamInt64Def> matchSet = new LinkedList<ICFBamInt64Def>();
		Iterator<ICFBamInt64Def> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamInt64Def> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Int64Def must not be \"" + subClassCode + "\"" );
			}
		}
	}
}
