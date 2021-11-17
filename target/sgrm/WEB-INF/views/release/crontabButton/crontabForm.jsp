<div class="row clearfix">
	<div class="col-sm-12">
		<h5 class="titulares">Detalles de Tarea</h5>
		<input type="hidden" id="crontab_id" value="1085">
	</div>
</div>

<div class="row clearfix">
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Ejecutar tarea con el usuario</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="user" name="user" value="" maxlength="50"
					class="form-control" placeholder="Ingrese un valor..">
					<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="user_error" class="error fieldError" for="user"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Activa?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="isActive" type="checkbox" value="0"><span
				class="lever"></span>Si
			</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Comando</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="commandCron" name="commandCron"
					maxlength="100" value="" class="form-control"
					placeholder="Ingrese un valor..">
					<div class="help-info">M&aacute;x. 100 caracteres</div>
			</div>
			<label id="commandCron_error" class="error fieldError"
				for="commandCron" style="visibility: hidden;">Valor
				requerido.</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Entrada del comando</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="commandEntry" name="commandEntry"
					maxlength="50" value="" class="form-control"
					placeholder="Ingrese un valor..">
					<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="commandEntry_error" class="error fieldError"
				for="commandEntry" style="visibility: hidden;">Valor
				requerido.</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Descripci&oacute;n</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="descriptionCron" name="descriptionCron"
					maxlength="50" value="" class="form-control"
					placeholder="Ingrese un valor..">
					<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="descriptionCron_error" class="error fieldError"
				for="descriptionCron" style="visibility: hidden;">Valor
				requerido.</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-sm-12">
		<h5 class="titulares">Cuando Ejecutar</h5>
	</div>
</div>

<div class="row clearfix timeSelect">
	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">

		<h2 class="grid">Minutos</h2>
		<i class="material-icons greyLigth select_all" data-type="minutes">select_all</i>
		<hr>
		<div id="minutesSelect" data-type="minutes" class="itemTime">
			<div id="0" class="col-xs-2 gridSquare">0</div>
			<div id="1" class="col-xs-2 gridSquare">1</div>
			<div id="2" class="col-xs-2 gridSquare">2</div>
			<div id="3" class="col-xs-2 gridSquare">3</div>
			<div id="4" class="col-xs-2 gridSquare">4</div>
			<div id="5" class="col-xs-2 gridSquare">5</div>
			<div id="6" class="col-xs-2 gridSquare">6</div>
			<div id="7" class="col-xs-2 gridSquare">7</div>
			<div id="8" class="col-xs-2 gridSquare">8</div>
			<div id="9" class="col-xs-2 gridSquare">9</div>
			<div id="10" class="col-xs-2 gridSquare">10</div>
			<div id="11" class="col-xs-2 gridSquare">11</div>
			<div id="12" class="col-xs-2 gridSquare">12</div>
			<div id="13" class="col-xs-2 gridSquare">13</div>
			<div id="14" class="col-xs-2 gridSquare">14</div>
			<div id="15" class="col-xs-2 gridSquare">15</div>
			<div id="16" class="col-xs-2 gridSquare">16</div>
			<div id="17" class="col-xs-2 gridSquare">17</div>
			<div id="18" class="col-xs-2 gridSquare">18</div>
			<div id="19" class="col-xs-2 gridSquare">19</div>
			<div id="20" class="col-xs-2 gridSquare">20</div>
			<div id="21" class="col-xs-2 gridSquare">21</div>
			<div id="22" class="col-xs-2 gridSquare">22</div>
			<div id="23" class="col-xs-2 gridSquare">23</div>
			<div id="24" class="col-xs-2 gridSquare">24</div>
			<div id="25" class="col-xs-2 gridSquare">25</div>
			<div id="26" class="col-xs-2 gridSquare">26</div>
			<div id="27" class="col-xs-2 gridSquare">27</div>
			<div id="28" class="col-xs-2 gridSquare">28</div>
			<div id="29" class="col-xs-2 gridSquare">29</div>
			<div id="30" class="col-xs-2 gridSquare">30</div>
			<div id="31" class="col-xs-2 gridSquare">31</div>
			<div id="32" class="col-xs-2 gridSquare">32</div>
			<div id="33" class="col-xs-2 gridSquare">33</div>
			<div id="34" class="col-xs-2 gridSquare">34</div>
			<div id="35" class="col-xs-2 gridSquare">35</div>
			<div id="36" class="col-xs-2 gridSquare">36</div>
			<div id="37" class="col-xs-2 gridSquare">37</div>
			<div id="38" class="col-xs-2 gridSquare">38</div>
			<div id="39" class="col-xs-2 gridSquare">39</div>
			<div id="40" class="col-xs-2 gridSquare">40</div>
			<div id="41" class="col-xs-2 gridSquare">41</div>
			<div id="42" class="col-xs-2 gridSquare">42</div>
			<div id="43" class="col-xs-2 gridSquare">43</div>
			<div id="44" class="col-xs-2 gridSquare">44</div>
			<div id="45" class="col-xs-2 gridSquare">45</div>
			<div id="46" class="col-xs-2 gridSquare">46</div>
			<div id="47" class="col-xs-2 gridSquare">47</div>
			<div id="48" class="col-xs-2 gridSquare">48</div>
			<div id="49" class="col-xs-2 gridSquare">49</div>
			<div id="50" class="col-xs-2 gridSquare">50</div>
			<div id="51" class="col-xs-2 gridSquare">51</div>
			<div id="52" class="col-xs-2 gridSquare">52</div>
			<div id="53" class="col-xs-2 gridSquare">53</div>
			<div id="54" class="col-xs-2 gridSquare">54</div>
			<div id="55" class="col-xs-2 gridSquare">55</div>
			<div id="56" class="col-xs-2 gridSquare">56</div>
			<div id="57" class="col-xs-2 gridSquare">57</div>
			<div id="58" class="col-xs-2 gridSquare">58</div>
			<div id="59" class="col-xs-2 gridSquare">59</div>
		</div>
		<label id="minutes_error" for="minutes" style="visibility: hidden;"
			class="error labelError fieldError p-l-10">Ingrese un valor</label>
	</div>
	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<h2 class="grid">Horas</h2>
		<i class="timeSelect material-icons greyLigth select_all"
			data-type="hour">select_all</i>
		<hr>
		<div id="hourSelect" data-type="hour" class="itemTime">
			<div id="0" class="col-xs-2 gridSquare">0</div>
			<div id="1" class="col-xs-2 gridSquare">1</div>
			<div id="2" class="col-xs-2 gridSquare">2</div>
			<div id="3" class="col-xs-2 gridSquare">3</div>
			<div id="4" class="col-xs-2 gridSquare">4</div>
			<div id="5" class="col-xs-2 gridSquare">5</div>
			<div id="6" class="col-xs-2 gridSquare">6</div>
			<div id="7" class="col-xs-2 gridSquare">7</div>
			<div id="8" class="col-xs-2 gridSquare">8</div>
			<div id="9" class="col-xs-2 gridSquare">9</div>
			<div id="10" class="col-xs-2 gridSquare">10</div>
			<div id="11" class="col-xs-2 gridSquare">11</div>
			<div id="12" class="col-xs-2 gridSquare">12</div>
			<div id="13" class="col-xs-2 gridSquare">13</div>
			<div id="14" class="col-xs-2 gridSquare">14</div>
			<div id="15" class="col-xs-2 gridSquare">15</div>
			<div id="16" class="col-xs-2 gridSquare">16</div>
			<div id="17" class="col-xs-2 gridSquare">17</div>
			<div id="18" class="col-xs-2 gridSquare">18</div>
			<div id="19" class="col-xs-2 gridSquare">19</div>
			<div id="20" class="col-xs-2 gridSquare">20</div>
			<div id="21" class="col-xs-2 gridSquare">21</div>
			<div id="22" class="col-xs-2 gridSquare">22</div>
			<div id="23" class="col-xs-2 gridSquare">23</div>
		</div>
		<label id="hour_error" for="hour" style="visibility: hidden;"
			class="error labelError fieldError p-l-10">Ingrese un valor</label>
	</div>
	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<h2 class="grid">D&iacute;as</h2>
		<i class=" material-icons greyLigth  select_all " data-type="days">select_all</i>
		<hr>
		<div id="daysSelect" data-type="days" class="itemTime">
			<div id="1" class="col-xs-2 gridSquare">1</div>
			<div id="2" class="col-xs-2 gridSquare">2</div>
			<div id="3" class="col-xs-2 gridSquare">3</div>
			<div id="4" class="col-xs-2 gridSquare">4</div>
			<div id="5" class="col-xs-2 gridSquare">5</div>
			<div id="6" class="col-xs-2 gridSquare">6</div>
			<div id="7" class="col-xs-2 gridSquare">7</div>
			<div id="8" class="col-xs-2 gridSquare">8</div>
			<div id="9" class="col-xs-2 gridSquare">9</div>
			<div id="10" class="col-xs-2 gridSquare">10</div>
			<div id="11" class="col-xs-2 gridSquare">11</div>
			<div id="12" class="col-xs-2 gridSquare">12</div>
			<div id="13" class="col-xs-2 gridSquare">13</div>
			<div id="14" class="col-xs-2 gridSquare">14</div>
			<div id="15" class="col-xs-2 gridSquare">15</div>
			<div id="16" class="col-xs-2 gridSquare">16</div>
			<div id="17" class="col-xs-2 gridSquare">17</div>
			<div id="18" class="col-xs-2 gridSquare">18</div>
			<div id="19" class="col-xs-2 gridSquare">19</div>
			<div id="20" class="col-xs-2 gridSquare">20</div>
			<div id="21" class="col-xs-2 gridSquare">21</div>
			<div id="22" class="col-xs-2 gridSquare">22</div>
			<div id="23" class="col-xs-2 gridSquare">23</div>
			<div id="24" class="col-xs-2 gridSquare">24</div>
			<div id="25" class="col-xs-2 gridSquare">25</div>
			<div id="26" class="col-xs-2 gridSquare">26</div>
			<div id="27" class="col-xs-2 gridSquare">27</div>
			<div id="28" class="col-xs-2 gridSquare">28</div>
			<div id="29" class="col-xs-2 gridSquare">29</div>
			<div id="30" class="col-xs-2 gridSquare">30</div>
			<div id="31" class="col-xs-2 gridSquare">31</div>
		</div>
		<label id="days_error" for="days" style="visibility: hidden;"
			class="error labelError fieldError p-l-10">Ingrese un valor</label>
	</div>

	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<h2 class="grid">Meses</h2>
		<i class=" material-icons greyLigth  select_all " data-type="month">select_all</i>
		<hr>
		<div id="monthSelect" data-type="month" class="itemTime">
			<div id="1" class="col-xs-6 gridSquare align-leftI">Enero</div>
			<div id="2" class="col-xs-6 gridSquare align-leftI">Febrero</div>
			<div id="3" class="col-xs-6 gridSquare align-leftI">Marzo</div>
			<div id="4" class="col-xs-6 gridSquare align-leftI">Abril</div>
			<div id="5" class="col-xs-6 gridSquare align-leftI">Mayo</div>
			<div id="6" class="col-xs-6 gridSquare align-leftI">Junio</div>
			<div id="7" class="col-xs-6 gridSquare align-leftI">Julio</div>
			<div id="8" class="col-xs-6 gridSquare align-leftI">Agosto</div>
			<div id="9" class="col-xs-6 gridSquare align-leftI">Septiembre</div>
			<div id="10" class="col-xs-6 gridSquare align-leftI">Octubre</div>
			<div id="11" class="col-xs-6 gridSquare align-leftI">Noviembre</div>
			<div id="12" class="col-xs-6 gridSquare align-leftI">Diciembre</div>
		</div>
		<label id="month_error" for="month" style="visibility: hidden;"
			class="error labelError fieldError p-l-10">Ingrese un valor</label>
	</div>

	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<h2 class="grid">D&iacute;a | Semana</h2>
		<i class=" material-icons greyLigth  select_all " data-type="weekDays">select_all</i>
		<hr>
		<div id="weekDaysSelect" data-type="weekDays" class="itemTime">
			<div id="1" class="col-xs-12 gridSquare align-leftI">Lunes</div>
			<div id="2" class="col-xs-12 gridSquare align-leftI">Martes</div>
			<div id="3" class="col-xs-12 gridSquare align-leftI">Miercoles</div>
			<div id="4" class="col-xs-12 gridSquare align-leftI">Jueves</div>
			<div id="5" class="col-xs-12 gridSquare align-leftI">Viernes</div>
			<div id="6" class="col-xs-12 gridSquare align-leftI">Sabado</div>
			<div id="7" class="col-xs-12 gridSquare align-leftI">Domingo</div>
		</div>
		<label id="weekDays_error" for="weekDays" style="visibility: hidden;"
			class="error labelError fieldError p-l-10">Ingrese un valor</label>
	</div>
</div>

