<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:message code="sponsor.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.invoices-tax-less-or-eq-21"/>
		</th>
		<td>
			<acme:print value="${invoicesTaxLessOrEq21}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.sponsorships-with-link"/>
		</th>
		<td>
			<acme:print value="${sponsorshipsWithLink}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:message code="sponsor.dashboard.form.title.sponsorship-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.average-sponsorships-amount"/>
		</th>
		<td>
			<acme:print value="${averageSponsorshipsAmount}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.deviation-sponsorships-amount"/>
		</th>
		<td>
			<acme:print value="${deviationSponsorshipsAmount}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.minimum-sponsorships-amount"/>
		</th>
		<td>
			<acme:print value="${minimumSponsorshipsAmount}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.maximum-sponsorships-amount"/>
		</th>
		<td>
			<acme:print value="${maximumSponsorshipsAmount}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:message code="sponsor.dashboard.form.title.invoice-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.average-invoices-quantity"/>
		</th>
		<td>
			<acme:print value="${averageInvoicesQuantity}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.deviation-invoices-quantity"/>
		</th>
		<td>
			<acme:print value="${deviationInvoicesQuantity}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.minimum-invoices-quantity"/>
		</th>
		<td>
			<acme:print value="${minimumInvoicesQuantity}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.maximum-invoices-quantity"/>
		</th>
		<td>
			<acme:print value="${maximumInvoicesQuantity}"/>
		</td>
	</tr>
</table>
