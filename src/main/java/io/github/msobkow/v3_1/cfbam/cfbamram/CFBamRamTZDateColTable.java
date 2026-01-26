
// Description: Java 25 in-memory RAM DbIO implementation for TZDateCol.

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
 *	CFBamRamTZDateColTable in-memory RAM DbIO implementation
 *	for TZDateCol.
 */
public class CFBamRamTZDateColTable
	implements ICFBamTZDateColTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffTZDateCol > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffTZDateCol >();
	private Map< CFBamBuffTZDateColByTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTZDateCol >> dictByTableIdx
		= new HashMap< CFBamBuffTZDateColByTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTZDateCol >>();

	public CFBamRamTZDateColTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createTZDateCol( ICFSecAuthorization Authorization,
		ICFBamTZDateCol Buff )
	{
		final String S_ProcName = "createTZDateCol";
		ICFBamValue tail = null;
		if( Buff.getClassCode().equals( "a87c" ) ) {
			ICFBamValue[] siblings = schema.getTableValue().readDerivedByScopeIdx( Authorization,
				Buff.getRequiredTableId() );
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
		}
		schema.getTableTZDateDef().createTZDateDef( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryValue().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamBuffTZDateColByTableIdxKey keyTableIdx = schema.getFactoryTZDateCol().newTableIdxKey();
		keyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTZDateDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"TZDateDef",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTZDateCol > subdictTableIdx;
		if( dictByTableIdx.containsKey( keyTableIdx ) ) {
			subdictTableIdx = dictByTableIdx.get( keyTableIdx );
		}
		else {
			subdictTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTZDateCol >();
			dictByTableIdx.put( keyTableIdx, subdictTableIdx );
		}
		subdictTableIdx.put( pkey, Buff );

		if( tail != null ) {
			String tailClassCode = tail.getClassCode();
			if( tailClassCode.equals( "a809" ) ) {
				ICFBamValue tailEdit = schema.getFactoryValue().newBuff();
				tailEdit.set( (ICFBamValue)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableValue().updateValue( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a80a" ) ) {
				ICFBamAtom tailEdit = schema.getFactoryAtom().newBuff();
				tailEdit.set( (ICFBamAtom)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableAtom().updateAtom( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a80b" ) ) {
				ICFBamBlobDef tailEdit = schema.getFactoryBlobDef().newBuff();
				tailEdit.set( (ICFBamBlobDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableBlobDef().updateBlobDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a80c" ) ) {
				ICFBamBlobType tailEdit = schema.getFactoryBlobType().newBuff();
				tailEdit.set( (ICFBamBlobType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableBlobType().updateBlobType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a86b" ) ) {
				ICFBamBlobCol tailEdit = schema.getFactoryBlobCol().newBuff();
				tailEdit.set( (ICFBamBlobCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableBlobCol().updateBlobCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a80d" ) ) {
				ICFBamBoolDef tailEdit = schema.getFactoryBoolDef().newBuff();
				tailEdit.set( (ICFBamBoolDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableBoolDef().updateBoolDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a80e" ) ) {
				ICFBamBoolType tailEdit = schema.getFactoryBoolType().newBuff();
				tailEdit.set( (ICFBamBoolType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableBoolType().updateBoolType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a86c" ) ) {
				ICFBamBoolCol tailEdit = schema.getFactoryBoolCol().newBuff();
				tailEdit.set( (ICFBamBoolCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableBoolCol().updateBoolCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a815" ) ) {
				ICFBamDateDef tailEdit = schema.getFactoryDateDef().newBuff();
				tailEdit.set( (ICFBamDateDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDateDef().updateDateDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a816" ) ) {
				ICFBamDateType tailEdit = schema.getFactoryDateType().newBuff();
				tailEdit.set( (ICFBamDateType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDateType().updateDateType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a86d" ) ) {
				ICFBamDateCol tailEdit = schema.getFactoryDateCol().newBuff();
				tailEdit.set( (ICFBamDateCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDateCol().updateDateCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a81c" ) ) {
				ICFBamDoubleDef tailEdit = schema.getFactoryDoubleDef().newBuff();
				tailEdit.set( (ICFBamDoubleDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDoubleDef().updateDoubleDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a81d" ) ) {
				ICFBamDoubleType tailEdit = schema.getFactoryDoubleType().newBuff();
				tailEdit.set( (ICFBamDoubleType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDoubleType().updateDoubleType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a86e" ) ) {
				ICFBamDoubleCol tailEdit = schema.getFactoryDoubleCol().newBuff();
				tailEdit.set( (ICFBamDoubleCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDoubleCol().updateDoubleCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a81f" ) ) {
				ICFBamFloatDef tailEdit = schema.getFactoryFloatDef().newBuff();
				tailEdit.set( (ICFBamFloatDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableFloatDef().updateFloatDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a820" ) ) {
				ICFBamFloatType tailEdit = schema.getFactoryFloatType().newBuff();
				tailEdit.set( (ICFBamFloatType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableFloatType().updateFloatType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a871" ) ) {
				ICFBamFloatCol tailEdit = schema.getFactoryFloatCol().newBuff();
				tailEdit.set( (ICFBamFloatCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableFloatCol().updateFloatCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a823" ) ) {
				ICFBamInt16Def tailEdit = schema.getFactoryInt16Def().newBuff();
				tailEdit.set( (ICFBamInt16Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt16Def().updateInt16Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a824" ) ) {
				ICFBamInt16Type tailEdit = schema.getFactoryInt16Type().newBuff();
				tailEdit.set( (ICFBamInt16Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt16Type().updateInt16Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a872" ) ) {
				ICFBamId16Gen tailEdit = schema.getFactoryId16Gen().newBuff();
				tailEdit.set( (ICFBamId16Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableId16Gen().updateId16Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a86f" ) ) {
				ICFBamEnumDef tailEdit = schema.getFactoryEnumDef().newBuff();
				tailEdit.set( (ICFBamEnumDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableEnumDef().updateEnumDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a870" ) ) {
				ICFBamEnumType tailEdit = schema.getFactoryEnumType().newBuff();
				tailEdit.set( (ICFBamEnumType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableEnumType().updateEnumType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a875" ) ) {
				ICFBamInt16Col tailEdit = schema.getFactoryInt16Col().newBuff();
				tailEdit.set( (ICFBamInt16Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt16Col().updateInt16Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a825" ) ) {
				ICFBamInt32Def tailEdit = schema.getFactoryInt32Def().newBuff();
				tailEdit.set( (ICFBamInt32Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt32Def().updateInt32Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a826" ) ) {
				ICFBamInt32Type tailEdit = schema.getFactoryInt32Type().newBuff();
				tailEdit.set( (ICFBamInt32Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt32Type().updateInt32Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a873" ) ) {
				ICFBamId32Gen tailEdit = schema.getFactoryId32Gen().newBuff();
				tailEdit.set( (ICFBamId32Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableId32Gen().updateId32Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a876" ) ) {
				ICFBamInt32Col tailEdit = schema.getFactoryInt32Col().newBuff();
				tailEdit.set( (ICFBamInt32Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt32Col().updateInt32Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a827" ) ) {
				ICFBamInt64Def tailEdit = schema.getFactoryInt64Def().newBuff();
				tailEdit.set( (ICFBamInt64Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt64Def().updateInt64Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a828" ) ) {
				ICFBamInt64Type tailEdit = schema.getFactoryInt64Type().newBuff();
				tailEdit.set( (ICFBamInt64Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt64Type().updateInt64Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a874" ) ) {
				ICFBamId64Gen tailEdit = schema.getFactoryId64Gen().newBuff();
				tailEdit.set( (ICFBamId64Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableId64Gen().updateId64Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a877" ) ) {
				ICFBamInt64Col tailEdit = schema.getFactoryInt64Col().newBuff();
				tailEdit.set( (ICFBamInt64Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableInt64Col().updateInt64Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a829" ) ) {
				ICFBamNmTokenDef tailEdit = schema.getFactoryNmTokenDef().newBuff();
				tailEdit.set( (ICFBamNmTokenDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a82a" ) ) {
				ICFBamNmTokenType tailEdit = schema.getFactoryNmTokenType().newBuff();
				tailEdit.set( (ICFBamNmTokenType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNmTokenType().updateNmTokenType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a878" ) ) {
				ICFBamNmTokenCol tailEdit = schema.getFactoryNmTokenCol().newBuff();
				tailEdit.set( (ICFBamNmTokenCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a82b" ) ) {
				ICFBamNmTokensDef tailEdit = schema.getFactoryNmTokensDef().newBuff();
				tailEdit.set( (ICFBamNmTokensDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a82c" ) ) {
				ICFBamNmTokensType tailEdit = schema.getFactoryNmTokensType().newBuff();
				tailEdit.set( (ICFBamNmTokensType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNmTokensType().updateNmTokensType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a879" ) ) {
				ICFBamNmTokensCol tailEdit = schema.getFactoryNmTokensCol().newBuff();
				tailEdit.set( (ICFBamNmTokensCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a82d" ) ) {
				ICFBamNumberDef tailEdit = schema.getFactoryNumberDef().newBuff();
				tailEdit.set( (ICFBamNumberDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNumberDef().updateNumberDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a82e" ) ) {
				ICFBamNumberType tailEdit = schema.getFactoryNumberType().newBuff();
				tailEdit.set( (ICFBamNumberType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNumberType().updateNumberType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a87a" ) ) {
				ICFBamNumberCol tailEdit = schema.getFactoryNumberCol().newBuff();
				tailEdit.set( (ICFBamNumberCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableNumberCol().updateNumberCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a839" ) ) {
				ICFBamDbKeyHash128Def tailEdit = schema.getFactoryDbKeyHash128Def().newBuff();
				tailEdit.set( (ICFBamDbKeyHash128Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a838" ) ) {
				ICFBamDbKeyHash128Col tailEdit = schema.getFactoryDbKeyHash128Col().newBuff();
				tailEdit.set( (ICFBamDbKeyHash128Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a83a" ) ) {
				ICFBamDbKeyHash128Type tailEdit = schema.getFactoryDbKeyHash128Type().newBuff();
				tailEdit.set( (ICFBamDbKeyHash128Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a83b" ) ) {
				ICFBamDbKeyHash128Gen tailEdit = schema.getFactoryDbKeyHash128Gen().newBuff();
				tailEdit.set( (ICFBamDbKeyHash128Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a83d" ) ) {
				ICFBamDbKeyHash160Def tailEdit = schema.getFactoryDbKeyHash160Def().newBuff();
				tailEdit.set( (ICFBamDbKeyHash160Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a83c" ) ) {
				ICFBamDbKeyHash160Col tailEdit = schema.getFactoryDbKeyHash160Col().newBuff();
				tailEdit.set( (ICFBamDbKeyHash160Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a83e" ) ) {
				ICFBamDbKeyHash160Type tailEdit = schema.getFactoryDbKeyHash160Type().newBuff();
				tailEdit.set( (ICFBamDbKeyHash160Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a83f" ) ) {
				ICFBamDbKeyHash160Gen tailEdit = schema.getFactoryDbKeyHash160Gen().newBuff();
				tailEdit.set( (ICFBamDbKeyHash160Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a841" ) ) {
				ICFBamDbKeyHash224Def tailEdit = schema.getFactoryDbKeyHash224Def().newBuff();
				tailEdit.set( (ICFBamDbKeyHash224Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a840" ) ) {
				ICFBamDbKeyHash224Col tailEdit = schema.getFactoryDbKeyHash224Col().newBuff();
				tailEdit.set( (ICFBamDbKeyHash224Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a842" ) ) {
				ICFBamDbKeyHash224Type tailEdit = schema.getFactoryDbKeyHash224Type().newBuff();
				tailEdit.set( (ICFBamDbKeyHash224Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a843" ) ) {
				ICFBamDbKeyHash224Gen tailEdit = schema.getFactoryDbKeyHash224Gen().newBuff();
				tailEdit.set( (ICFBamDbKeyHash224Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a845" ) ) {
				ICFBamDbKeyHash256Def tailEdit = schema.getFactoryDbKeyHash256Def().newBuff();
				tailEdit.set( (ICFBamDbKeyHash256Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a844" ) ) {
				ICFBamDbKeyHash256Col tailEdit = schema.getFactoryDbKeyHash256Col().newBuff();
				tailEdit.set( (ICFBamDbKeyHash256Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a846" ) ) {
				ICFBamDbKeyHash256Type tailEdit = schema.getFactoryDbKeyHash256Type().newBuff();
				tailEdit.set( (ICFBamDbKeyHash256Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a847" ) ) {
				ICFBamDbKeyHash256Gen tailEdit = schema.getFactoryDbKeyHash256Gen().newBuff();
				tailEdit.set( (ICFBamDbKeyHash256Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a849" ) ) {
				ICFBamDbKeyHash384Def tailEdit = schema.getFactoryDbKeyHash384Def().newBuff();
				tailEdit.set( (ICFBamDbKeyHash384Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a848" ) ) {
				ICFBamDbKeyHash384Col tailEdit = schema.getFactoryDbKeyHash384Col().newBuff();
				tailEdit.set( (ICFBamDbKeyHash384Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a84a" ) ) {
				ICFBamDbKeyHash384Type tailEdit = schema.getFactoryDbKeyHash384Type().newBuff();
				tailEdit.set( (ICFBamDbKeyHash384Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a84b" ) ) {
				ICFBamDbKeyHash384Gen tailEdit = schema.getFactoryDbKeyHash384Gen().newBuff();
				tailEdit.set( (ICFBamDbKeyHash384Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a84d" ) ) {
				ICFBamDbKeyHash512Def tailEdit = schema.getFactoryDbKeyHash512Def().newBuff();
				tailEdit.set( (ICFBamDbKeyHash512Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a84c" ) ) {
				ICFBamDbKeyHash512Col tailEdit = schema.getFactoryDbKeyHash512Col().newBuff();
				tailEdit.set( (ICFBamDbKeyHash512Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a84e" ) ) {
				ICFBamDbKeyHash512Type tailEdit = schema.getFactoryDbKeyHash512Type().newBuff();
				tailEdit.set( (ICFBamDbKeyHash512Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a84f" ) ) {
				ICFBamDbKeyHash512Gen tailEdit = schema.getFactoryDbKeyHash512Gen().newBuff();
				tailEdit.set( (ICFBamDbKeyHash512Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a850" ) ) {
				ICFBamStringDef tailEdit = schema.getFactoryStringDef().newBuff();
				tailEdit.set( (ICFBamStringDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableStringDef().updateStringDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a851" ) ) {
				ICFBamStringType tailEdit = schema.getFactoryStringType().newBuff();
				tailEdit.set( (ICFBamStringType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableStringType().updateStringType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a87b" ) ) {
				ICFBamStringCol tailEdit = schema.getFactoryStringCol().newBuff();
				tailEdit.set( (ICFBamStringCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableStringCol().updateStringCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a852" ) ) {
				ICFBamTZDateDef tailEdit = schema.getFactoryTZDateDef().newBuff();
				tailEdit.set( (ICFBamTZDateDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZDateDef().updateTZDateDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a853" ) ) {
				ICFBamTZDateType tailEdit = schema.getFactoryTZDateType().newBuff();
				tailEdit.set( (ICFBamTZDateType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZDateType().updateTZDateType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a87c" ) ) {
				ICFBamTZDateCol tailEdit = schema.getFactoryTZDateCol().newBuff();
				tailEdit.set( (ICFBamTZDateCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZDateCol().updateTZDateCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a854" ) ) {
				ICFBamTZTimeDef tailEdit = schema.getFactoryTZTimeDef().newBuff();
				tailEdit.set( (ICFBamTZTimeDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a855" ) ) {
				ICFBamTZTimeType tailEdit = schema.getFactoryTZTimeType().newBuff();
				tailEdit.set( (ICFBamTZTimeType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZTimeType().updateTZTimeType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a87d" ) ) {
				ICFBamTZTimeCol tailEdit = schema.getFactoryTZTimeCol().newBuff();
				tailEdit.set( (ICFBamTZTimeCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a856" ) ) {
				ICFBamTZTimestampDef tailEdit = schema.getFactoryTZTimestampDef().newBuff();
				tailEdit.set( (ICFBamTZTimestampDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a857" ) ) {
				ICFBamTZTimestampType tailEdit = schema.getFactoryTZTimestampType().newBuff();
				tailEdit.set( (ICFBamTZTimestampType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a87e" ) ) {
				ICFBamTZTimestampCol tailEdit = schema.getFactoryTZTimestampCol().newBuff();
				tailEdit.set( (ICFBamTZTimestampCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a859" ) ) {
				ICFBamTextDef tailEdit = schema.getFactoryTextDef().newBuff();
				tailEdit.set( (ICFBamTextDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTextDef().updateTextDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a85a" ) ) {
				ICFBamTextType tailEdit = schema.getFactoryTextType().newBuff();
				tailEdit.set( (ICFBamTextType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTextType().updateTextType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a87f" ) ) {
				ICFBamTextCol tailEdit = schema.getFactoryTextCol().newBuff();
				tailEdit.set( (ICFBamTextCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTextCol().updateTextCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a85b" ) ) {
				ICFBamTimeDef tailEdit = schema.getFactoryTimeDef().newBuff();
				tailEdit.set( (ICFBamTimeDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTimeDef().updateTimeDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a85c" ) ) {
				ICFBamTimeType tailEdit = schema.getFactoryTimeType().newBuff();
				tailEdit.set( (ICFBamTimeType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTimeType().updateTimeType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a880" ) ) {
				ICFBamTimeCol tailEdit = schema.getFactoryTimeCol().newBuff();
				tailEdit.set( (ICFBamTimeCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTimeCol().updateTimeCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a85d" ) ) {
				ICFBamTimestampDef tailEdit = schema.getFactoryTimestampDef().newBuff();
				tailEdit.set( (ICFBamTimestampDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTimestampDef().updateTimestampDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a85e" ) ) {
				ICFBamTimestampType tailEdit = schema.getFactoryTimestampType().newBuff();
				tailEdit.set( (ICFBamTimestampType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTimestampType().updateTimestampType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a881" ) ) {
				ICFBamTimestampCol tailEdit = schema.getFactoryTimestampCol().newBuff();
				tailEdit.set( (ICFBamTimestampCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTimestampCol().updateTimestampCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a85f" ) ) {
				ICFBamTokenDef tailEdit = schema.getFactoryTokenDef().newBuff();
				tailEdit.set( (ICFBamTokenDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTokenDef().updateTokenDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a860" ) ) {
				ICFBamTokenType tailEdit = schema.getFactoryTokenType().newBuff();
				tailEdit.set( (ICFBamTokenType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTokenType().updateTokenType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a882" ) ) {
				ICFBamTokenCol tailEdit = schema.getFactoryTokenCol().newBuff();
				tailEdit.set( (ICFBamTokenCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTokenCol().updateTokenCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a861" ) ) {
				ICFBamUInt16Def tailEdit = schema.getFactoryUInt16Def().newBuff();
				tailEdit.set( (ICFBamUInt16Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt16Def().updateUInt16Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a862" ) ) {
				ICFBamUInt16Type tailEdit = schema.getFactoryUInt16Type().newBuff();
				tailEdit.set( (ICFBamUInt16Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt16Type().updateUInt16Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a883" ) ) {
				ICFBamUInt16Col tailEdit = schema.getFactoryUInt16Col().newBuff();
				tailEdit.set( (ICFBamUInt16Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt16Col().updateUInt16Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a863" ) ) {
				ICFBamUInt32Def tailEdit = schema.getFactoryUInt32Def().newBuff();
				tailEdit.set( (ICFBamUInt32Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt32Def().updateUInt32Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a864" ) ) {
				ICFBamUInt32Type tailEdit = schema.getFactoryUInt32Type().newBuff();
				tailEdit.set( (ICFBamUInt32Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt32Type().updateUInt32Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a884" ) ) {
				ICFBamUInt32Col tailEdit = schema.getFactoryUInt32Col().newBuff();
				tailEdit.set( (ICFBamUInt32Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt32Col().updateUInt32Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a865" ) ) {
				ICFBamUInt64Def tailEdit = schema.getFactoryUInt64Def().newBuff();
				tailEdit.set( (ICFBamUInt64Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt64Def().updateUInt64Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a866" ) ) {
				ICFBamUInt64Type tailEdit = schema.getFactoryUInt64Type().newBuff();
				tailEdit.set( (ICFBamUInt64Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt64Type().updateUInt64Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a885" ) ) {
				ICFBamUInt64Col tailEdit = schema.getFactoryUInt64Col().newBuff();
				tailEdit.set( (ICFBamUInt64Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUInt64Col().updateUInt64Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a867" ) ) {
				ICFBamUuidDef tailEdit = schema.getFactoryUuidDef().newBuff();
				tailEdit.set( (ICFBamUuidDef)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuidDef().updateUuidDef( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a869" ) ) {
				ICFBamUuidType tailEdit = schema.getFactoryUuidType().newBuff();
				tailEdit.set( (ICFBamUuidType)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuidType().updateUuidType( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a888" ) ) {
				ICFBamUuidGen tailEdit = schema.getFactoryUuidGen().newBuff();
				tailEdit.set( (ICFBamUuidGen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuidGen().updateUuidGen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a886" ) ) {
				ICFBamUuidCol tailEdit = schema.getFactoryUuidCol().newBuff();
				tailEdit.set( (ICFBamUuidCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuidCol().updateUuidCol( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a868" ) ) {
				ICFBamUuid6Def tailEdit = schema.getFactoryUuid6Def().newBuff();
				tailEdit.set( (ICFBamUuid6Def)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuid6Def().updateUuid6Def( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a86a" ) ) {
				ICFBamUuid6Type tailEdit = schema.getFactoryUuid6Type().newBuff();
				tailEdit.set( (ICFBamUuid6Type)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuid6Type().updateUuid6Type( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a889" ) ) {
				ICFBamUuid6Gen tailEdit = schema.getFactoryUuid6Gen().newBuff();
				tailEdit.set( (ICFBamUuid6Gen)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a887" ) ) {
				ICFBamUuid6Col tailEdit = schema.getFactoryUuid6Col().newBuff();
				tailEdit.set( (ICFBamUuid6Col)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableUuid6Col().updateUuid6Col( Authorization, tailEdit );
			}
			else if( tailClassCode.equals( "a858" ) ) {
				ICFBamTableCol tailEdit = schema.getFactoryTableCol().newBuff();
				tailEdit.set( (ICFBamTableCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableTableCol().updateTableCol( Authorization, tailEdit );
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode " + tailClassCode );
			}
		}
	}

	public ICFBamTZDateCol readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTZDateCol.readDerived";
		ICFBamTZDateCol buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamTZDateCol lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTZDateCol.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryValue().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamTZDateCol buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamTZDateCol[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTZDateCol.readAllDerived";
		ICFBamTZDateCol[] retList = new ICFBamTZDateCol[ dictByPKey.values().size() ];
		Iterator< ICFBamTZDateCol > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamTZDateCol readDerivedByUNameIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamTZDateCol ) {
			return( (ICFBamTZDateCol)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamTZDateCol[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTZDateCol ) ) {
					filteredList.add( (ICFBamTZDateCol)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
		}
	}

	public ICFBamTZDateCol[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTZDateCol ) ) {
					filteredList.add( (ICFBamTZDateCol)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
		}
	}

	public ICFBamTZDateCol[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTZDateCol ) ) {
					filteredList.add( (ICFBamTZDateCol)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
		}
	}

	public ICFBamTZDateCol[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTZDateCol ) ) {
					filteredList.add( (ICFBamTZDateCol)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
		}
	}

	public ICFBamTZDateCol[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTZDateCol ) ) {
					filteredList.add( (ICFBamTZDateCol)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
		}
	}

	public ICFBamTZDateCol[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTZDateCol ) ) {
					filteredList.add( (ICFBamTZDateCol)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
		}
	}

	public ICFBamTZDateCol[] readDerivedByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamTZDateCol.readDerivedByTableIdx";
		CFBamBuffTZDateColByTableIdxKey key = schema.getFactoryTZDateCol().newTableIdxKey();
		key.setRequiredTableId( TableId );

		ICFBamTZDateCol[] recArray;
		if( dictByTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTZDateCol > subdictTableIdx
				= dictByTableIdx.get( key );
			recArray = new ICFBamTZDateCol[ subdictTableIdx.size() ];
			Iterator< ICFBamTZDateCol > iter = subdictTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTZDateCol > subdictTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTZDateCol >();
			dictByTableIdx.put( key, subdictTableIdx );
			recArray = new ICFBamTZDateCol[0];
		}
		return( recArray );
	}

	public ICFBamTZDateCol readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryValue().newPKey();
		key.setRequiredId( Id );

		ICFBamTZDateCol buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamTZDateCol readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTZDateCol.readBuff";
		ICFBamTZDateCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a87c" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamTZDateCol lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamTZDateCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a87c" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamTZDateCol[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTZDateCol.readAllBuff";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a87c" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	public ICFBamTZDateCol readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByIdIdx() ";
		ICFBamTZDateCol buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
			return( (ICFBamTZDateCol)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamTZDateCol readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByUNameIdx() ";
		ICFBamTZDateCol buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
			return( (ICFBamTZDateCol)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamTZDateCol[] readBuffByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByScopeIdx() ";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamTZDateCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	public ICFBamTZDateCol[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByDefSchemaIdx() ";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamTZDateCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	public ICFBamTZDateCol[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByPrevIdx() ";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamTZDateCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	public ICFBamTZDateCol[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByNextIdx() ";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamTZDateCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	public ICFBamTZDateCol[] readBuffByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByContPrevIdx() ";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamTZDateCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	public ICFBamTZDateCol[] readBuffByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByContNextIdx() ";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a809" ) ) {
				filteredList.add( (ICFBamTZDateCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	public ICFBamTZDateCol[] readBuffByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamTZDateCol.readBuffByTableIdx() ";
		ICFBamTZDateCol buff;
		ArrayList<ICFBamTZDateCol> filteredList = new ArrayList<ICFBamTZDateCol>();
		ICFBamTZDateCol[] buffList = readDerivedByTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a87c" ) ) {
				filteredList.add( (ICFBamTZDateCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTZDateCol[0] ) );
	}

	/**
	 *	Read a page array of the specific TZDateCol buffer instances identified by the duplicate key TableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The TZDateCol key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamTZDateCol[] pageBuffByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamTZDateCol moveBuffUp( ICFSecAuthorization Authorization,
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
			return( (CFBamTZDateColBuff)cur );
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

		return( (CFBamTZDateColBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamTZDateCol moveBuffDown( ICFSecAuthorization Authorization,
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
			return( (CFBamTZDateColBuff)cur );
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

		return( (CFBamTZDateColBuff)editCur );
	}

	public void updateTZDateCol( ICFSecAuthorization Authorization,
		ICFBamTZDateCol Buff )
	{
		schema.getTableTZDateDef().updateTZDateDef( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryValue().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamTZDateCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTZDateCol",
				"Existing record not found",
				"TZDateCol",
				pkey );
		}
		CFBamBuffTZDateColByTableIdxKey existingKeyTableIdx = schema.getFactoryTZDateCol().newTableIdxKey();
		existingKeyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffTZDateColByTableIdxKey newKeyTableIdx = schema.getFactoryTZDateCol().newTableIdxKey();
		newKeyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTZDateDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTZDateCol",
						"Superclass",
						"SuperClass",
						"TZDateDef",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTZDateCol",
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffTZDateCol > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByTableIdx.get( existingKeyTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByTableIdx.containsKey( newKeyTableIdx ) ) {
			subdict = dictByTableIdx.get( newKeyTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTZDateCol >();
			dictByTableIdx.put( newKeyTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteTZDateCol( ICFSecAuthorization Authorization,
		ICFBamTZDateCol Buff )
	{
		final String S_ProcName = "CFBamRamTZDateColTable.deleteTZDateCol() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryValue().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamTZDateCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTZDateCol",
				pkey );
		}
		CFLibDbKeyHash256 varTableId = existing.getRequiredTableId();
		CFBamTableBuff container = schema.getTableTable().readDerivedByIdIdx( Authorization,
			varTableId );
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
		CFBamBuffTZDateColByTableIdxKey keyTableIdx = schema.getFactoryTZDateCol().newTableIdxKey();
		keyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffTZDateCol > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByTableIdx.get( keyTableIdx );
		subdict.remove( pkey );

		schema.getTableTZDateDef().deleteTZDateDef( Authorization,
			Buff );
	}
	public void deleteTZDateColByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffTZDateColByTableIdxKey key = schema.getFactoryTZDateCol().newTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteTZDateColByTableIdx( Authorization, key );
	}

	public void deleteTZDateColByTableIdx( ICFSecAuthorization Authorization,
		ICFBamTZDateColByTableIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryValue().newPKey();
		key.setRequiredId( argId );
		deleteTZDateColByIdIdx( Authorization, key );
	}

	public void deleteTZDateColByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamTZDateCol cur;
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffValueByUNameIdxKey key = schema.getFactoryValue().newUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteTZDateColByUNameIdx( Authorization, key );
	}

	public void deleteTZDateColByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamValueByUNameIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffValueByScopeIdxKey key = schema.getFactoryValue().newScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteTZDateColByScopeIdx( Authorization, key );
	}

	public void deleteTZDateColByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamValueByScopeIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffValueByDefSchemaIdxKey key = schema.getFactoryValue().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteTZDateColByDefSchemaIdx( Authorization, key );
	}

	public void deleteTZDateColByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamValueByDefSchemaIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByPrevIdxKey key = schema.getFactoryValue().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteTZDateColByPrevIdx( Authorization, key );
	}

	public void deleteTZDateColByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByPrevIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByNextIdxKey key = schema.getFactoryValue().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteTZDateColByNextIdx( Authorization, key );
	}

	public void deleteTZDateColByNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByNextIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByContPrevIdxKey key = schema.getFactoryValue().newContPrevIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalPrevId( argPrevId );
		deleteTZDateColByContPrevIdx( Authorization, key );
	}

	public void deleteTZDateColByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContPrevIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}

	public void deleteTZDateColByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByContNextIdxKey key = schema.getFactoryValue().newContNextIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalNextId( argNextId );
		deleteTZDateColByContNextIdx( Authorization, key );
	}

	public void deleteTZDateColByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContNextIdxKey argKey )
	{
		ICFBamTZDateCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamTZDateCol> matchSet = new LinkedList<ICFBamTZDateCol>();
		Iterator<ICFBamTZDateCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamTZDateCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTZDateCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTZDateCol( Authorization, cur );
		}
	}
}
