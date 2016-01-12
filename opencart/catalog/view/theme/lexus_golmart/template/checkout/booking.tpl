<?php echo $header; ?>
<div class="container">
<ul class="breadcrumb">
    <?php foreach ($breadcrumbs as $breadcrumb) { ?>
    <li><a href="<?php echo $breadcrumb['href']; ?>"><?php echo $breadcrumb['text']; ?></a></li>
    <?php } ?>
</ul>
<?php if ($error_warning) { ?>
<div class="alert alert-danger"><i class="fa fa-exclamation-circle"></i> <?php echo $error_warning; ?>
    <button type="button" class="close" data-dismiss="alert">&times;</button>
</div>
<?php } ?>
<div class="row">
<?php echo $column_left; ?>
<?php if ($column_left && $column_right) { ?>
<?php $class = 'col-sm-6'; ?>
<?php } elseif ($column_left || $column_right) { ?>
<?php $class = 'col-sm-9'; ?>
<?php } else { ?>
<?php $class = 'col-sm-12'; ?>
<?php } ?>
<div id="content" class="<?php echo $class; ?>">
<?php echo $content_top; ?>
    <div id="row">
    <!-- Cart-->
    <fieldset id="cart">
        <legend><?php echo $text_cart_info; ?></legend>
        <form action="<?php echo $action; ?>" method="post" enctype="multipart/form-data">
            <div class="table-responsive">
                <table class="table table-bordered bg-white">
                    <thead>
                    <tr>
                        <td class="text-center"><?php echo $column_image; ?></td>
                        <td class="text-center"><?php echo $column_name; ?></td>
                        <td class="text-center"><?php echo $column_model; ?></td>
                        <td class="text-center"><?php echo $column_quantity; ?></td>
                        <td class="text-center"><?php echo $column_price; ?></td>
                        <td class="text-center"><?php echo $column_total; ?></td>
                    </tr>
                    </thead>
                    <tbody>
                    <?php foreach ($products as $product) { ?>
                    <tr>
                        <td class="text-center"><?php if ($product['thumb']) { ?>
                            <a href="<?php echo $product['href']; ?>"><img
                                        src="<?php echo $product['thumb']; ?>"
                                        alt="<?php echo $product['name']; ?>"
                                        title="<?php echo $product['name']; ?>" class="img-thumbnail"/></a>
                            <?php } ?></td>
                        <td class="text-center"><a
                                    href="<?php echo $product['href']; ?>"><?php echo $product['name']; ?></a>
                            <?php if (!$product['stock']) { ?>
                            <span class="text-danger">***</span>
                            <?php } ?>
                            <?php if ($product['option']) { ?>
                            <?php foreach ($product['option'] as $option) { ?>
                            <br/>
                            <small><?php echo $option['name']; ?>: <?php echo $option['value']; ?></small>
                            <?php } ?>
                            <?php } ?>
                            <?php if ($product['reward']) { ?>
                            <br/>
                            <small><?php echo $product['reward']; ?></small>
                            <?php } ?>
                            <?php if ($product['recurring']) { ?>
                            <br/>
                            <span class="label label-info"><?php echo $text_recurring_item; ?></span>
                            <small><?php echo $product['recurring']; ?></small>
                            <?php } ?></td>
                        <td class="text-center"><?php echo $product['model']; ?></td>
                        <td class="text-center">
                            <div class="input-group btn-block" style="max-width: 200px;">
                                <input type="text" name="quantity[<?php echo $product['key']; ?>]"
                                       value="<?php echo $product['quantity']; ?>" size="1"
                                       class="form-control"/>
                    <span class="input-group-btn">
                    <button type="submit" data-toggle="tooltip" title="<?php echo $button_update; ?>"
                            class="btn btn-default"><i class="fa fa-refresh"></i></button>
                    <button type="button" data-toggle="tooltip" title="<?php echo $button_remove; ?>"
                            class="btn btn-danger" onclick="cart.remove('<?php echo $product['key']; ?>');"><i
                                class="fa fa-times-circle"></i></button></span></div>
                        </td>
                        <td class="text-center"><?php echo $product['price']; ?></td>
                        <td class="text-center"><?php echo $product['total']; ?></td>
                    </tr>
                    <?php } ?>
                    <?php foreach ($vouchers as $vouchers) { ?>
                    <tr>
                        <td></td>
                        <td class="text-center"><?php echo $vouchers['description']; ?></td>
                        <td class="text-center"></td>
                        <td class="text-center">
                            <div class="input-group btn-block" style="max-width: 200px;">
                                <input type="text" name="" value="1" size="1" disabled="disabled"
                                       class="form-control"/>
                                            <span class="input-group-btn"><button type="button" data-toggle="tooltip"
                                                                                  title="<?php echo $button_remove; ?>"
                                                                                  class="btn btn-danger"
                                                                                  onclick="voucher.remove('<?php echo $vouchers['key']; ?>');">
                                                    <i class="fa fa-times-circle"></i></button></span></div>
                        </td>
                        <td class="text-right"><?php echo $vouchers['amount']; ?></td>
                    </tr>
                    <?php } ?>
                    </tbody>
                </table>
            </div>
        </form>

        <div class="row" class="space-padding-20">
            <div class="col-sm-4 col-sm-offset-8">
                <table class="table table-bordered bg-white">
                    <?php foreach ($totals as $total) { ?>
                    <tr>
                        <td class="text-right"><strong><?php echo $total['title']; ?>:</strong></td>
                        <td class="text-right"><?php echo $total['text']; ?></td>
                    </tr>
                    <?php } ?>
                </table>
            </div>
        </div>
    </fieldset>
    <!-- End Cart-->

    <!-- Thông tin khách hàng -->

    <fieldset id="account">
        <legend><?php echo $text_customer_info; ?></legend>
        <div class="form-group required">
            <label class="control-label" for="input-payment-firstname"><?php echo $entry_firstname; ?></label>
            <input type="text" name="firstname" value="<?php echo $firstname; ?>"
                   placeholder="<?php echo $entry_firstname_placeholder; ?>" id="input-payment-firstname" class="form-control"/>
        </div>
        <div class="form-group required">
            <label class="control-label" for="input-payment-telephone"><?php echo $entry_telephone; ?></label>
            <input type="text" name="telephone" value="<?php echo $telephone; ?>"
                   placeholder="<?php echo $entry_telephone_placeholder; ?>" id="input-payment-telephone" class="form-control"/>
        </div>
        <div class="form-group required">
            <label class="control-label" for="input-payment-email"><?php echo $entry_email; ?></label>
            <input type="text" name="email" value="<?php echo $email; ?>" placeholder="<?php echo $entry_email_placeholder; ?>"
                   id="input-payment-email" class="form-control"/>
        </div>
        <div class="form-group required">
            <label class="control-label" for="input-payment-address-1"><?php echo $entry_address; ?></label>
            <input type="text" name="address_1" value="<?php echo $address_1; ?>"
                   placeholder="<?php echo $entry_address_placeholder; ?>" id="input-payment-address-1" class="form-control"/>
        </div>
        <div class="form-group">
            <label class="control-label" for="comment"><?php echo $text_comments; ?></label>
            <textarea name="comment" rows="8" class="form-control" placeholder="<?php echo $text_comments_placeholder; ?>"><?php echo $comment; ?></textarea>
        </div>
    </fieldset>

    <!-- End hông tin khách hàng -->

    <!-- Hình thức thanh toán -->
    <?php if ($payment_methods) { ?>
    <fieldset id="account">
        <legend><?php echo $text_payment_method; ?></legend>
    <?php foreach ($payment_methods as $payment_method) { ?>
    <div class="radio">
        <label>
            <?php if ($payment_method['code'] == $code || !$code) { ?>
            <?php $code = $payment_method['code']; ?>
            <input type="radio" name="payment_method" value="<?php echo $payment_method['code']; ?>" checked="checked"/>
            <?php } else { ?>
            <input type="radio" name="payment_method" value="<?php echo $payment_method['code']; ?>"/>
            <?php } ?>
            <?php echo $payment_method['title']; ?>
            <?php if ($payment_method['terms']) { ?>
            (<?php echo $payment_method['terms']; ?>)
            <?php } ?>
        </label>
    </div>
    <?php } ?>
    <?php } ?>
    <!-- End Hình thức thanh toán -->
    </div>
</div>


<div class="buttons">
    <div class="pull-left">
        <input type="button" value="<?php echo $button_booking; ?>" id="button-booking" class="btn btn-primary"/>
    </div>
</div>


<?php echo $content_bottom; ?>
</div> <!-- End content -->
<?php echo $column_right; ?>
</div> <!-- End row -->
</div><!-- End container -->

<script type="text/javascript"><!--
$(document).on('change', 'input[name=\'account\']', function () {
    if ($('#collapse-payment-address').parent().find('.panel-heading .panel-title > *').is('a')) {
        if (this.value == 'register') {
            $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_account; ?> <i class="fa fa-caret-down"></i></a>');
        } else {
            $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_address; ?> <i class="fa fa-caret-down"></i></a>');
        }
    } else {
        if (this.value == 'register') {
            $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_account; ?>');
        } else {
            $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_address; ?>');
        }
    }
});

<
? php
if (!$logged) {
    ?
>
    $(document).ready(function () {
        $.ajax({
            url: 'index.php?route=checkout/login',
            dataType: 'html',
            success: function (html) {
                $('#collapse-checkout-option .panel-body').html(html);

                $('#collapse-checkout-option').parent().find('.panel-heading .panel-title').html('<a href="#collapse-checkout-option" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_option; ?> <i class="fa fa-caret-down"></i></a>');

                $('a[href=\'#collapse-checkout-option\']').trigger('click');
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    });
<
    ? php
} else {
    ?
>
    $(document).ready(function () {
        $.ajax({
            url: 'index.php?route=checkout/payment_address',
            dataType: 'html',
            success: function (html) {
                $('#collapse-payment-address .panel-body').html(html);

                $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_address; ?> <i class="fa fa-caret-down"></i></a>');

                $('a[href=\'#collapse-payment-address\']').trigger('click');
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
            }
        });
    });
<
    ? php
}
?
>

// Checkout
$(document).delegate('#button-account', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/' + $('input[name=\'account\']:checked').val(),
        dataType: 'html',
        beforeSend: function () {
            $('#button-account').button('loading');
        },
        complete: function () {
            $('#button-account').button('reset');
        },
        success: function (html) {
            $('.alert, .text-danger').remove();

            $('#collapse-payment-address .panel-body').html(html);

            if ($('input[name=\'account\']:checked').val() == 'register') {
                $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_account; ?> <i class="fa fa-caret-down"></i></a>');
            } else {
                $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_address; ?> <i class="fa fa-caret-down"></i></a>');
            }

            $('a[href=\'#collapse-payment-address\']').trigger('click');
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

// Login
$(document).delegate('#button-login', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/login/save',
        type: 'post',
        data: $('#collapse-checkout-option :input'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-login').button('loading');
        },
        complete: function () {
            $('#button-login').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();
            $('.form-group').removeClass('has-error');

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                $('#collapse-checkout-option .panel-body').prepend('<div class="alert alert-danger"><i class="fa fa-exclamation-circle"></i> ' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');

                // Highlight any found errors
                $('input[name=\'email\']').parent().addClass('has-error');
                $('input[name=\'password\']').parent().addClass('has-error');
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

// Register
$(document).delegate('#button-register', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/register/save',
        type: 'post',
        data: $('#collapse-payment-address input[type=\'text\'], #collapse-payment-address input[type=\'date\'], #collapse-payment-address input[type=\'datetime-local\'], #collapse-payment-address input[type=\'time\'], #collapse-payment-address input[type=\'password\'], #collapse-payment-address input[type=\'hidden\'], #collapse-payment-address input[type=\'checkbox\']:checked, #collapse-payment-address input[type=\'radio\']:checked, #collapse-payment-address textarea, #collapse-payment-address select'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-register').button('loading');
        },
        complete: function () {
            $('#button-register').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();
            $('.form-group').removeClass('has-error');

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                if (json['error']['warning']) {
                    $('#collapse-payment-address .panel-body').prepend('<div class="alert alert-danger"><i class="fa fa-exclamation-circle"></i> ' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                }

                for (i in json['error']) {
                    var element = $('#input-payment-' + i.replace('_', '-'));

                    if ($(element).parent().hasClass('input-group')) {
                        $(element).parent().after('<div class="text-danger">' + json['error'][i] + '</div>');
                    } else {
                        $(element).after('<div class="text-danger">' + json['error'][i] + '</div>');
                    }
                }

                // Highlight any found errors
                $('.text-danger').parent().addClass('has-error');
            } else {
            <
                ? php
                if ($shipping_required) {
                    ?
                >
                    var shipping_address = $('#payment-address input[name=\'shipping_address\']:checked').prop('value');

                    if (shipping_address) {
                        $.ajax({
                            url: 'index.php?route=checkout/shipping_method',
                            dataType: 'html',
                            success: function (html) {
                                // Add the shipping address
                                $.ajax({
                                    url: 'index.php?route=checkout/shipping_address',
                                    dataType: 'html',
                                    success: function (html) {
                                        $('#collapse-shipping-address .panel-body').html(html);

                                        $('#collapse-shipping-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_address; ?> <i class="fa fa-caret-down"></i></a>');
                                    },
                                    error: function (xhr, ajaxOptions, thrownError) {
                                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                                    }
                                });

                                $('#collapse-shipping-method .panel-body').html(html);

                                $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_method; ?> <i class="fa fa-caret-down"></i></a>');

                                $('a[href=\'#collapse-shipping-method\']').trigger('click');

                                $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_shipping_method; ?>');
                                $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_method; ?>');
                                $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                            }
                        });
                    } else {
                        $.ajax({
                            url: 'index.php?route=checkout/shipping_address',
                            dataType: 'html',
                            success: function (html) {
                                $('#collapse-shipping-address .panel-body').html(html);

                                $('#collapse-shipping-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_address; ?> <i class="fa fa-caret-down"></i></a>');

                                $('a[href=\'#collapse-shipping-address\']').trigger('click');

                                $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_shipping_method; ?>');
                                $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_method; ?>');
                                $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                            }
                        });
                    }
                <
                    ? php
                } else {
                    ?
                >
                    $.ajax({
                        url: 'index.php?route=checkout/payment_method',
                        dataType: 'html',
                        success: function (html) {
                            $('#collapse-payment-method .panel-body').html(html);

                            $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_method; ?> <i class="fa fa-caret-down"></i></a>');

                            $('a[href=\'#collapse-payment-method\']').trigger('click');

                            $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                        }
                    });
                <
                    ? php
                }
                ?
            >

                $.ajax({
                    url: 'index.php?route=checkout/payment_address',
                    dataType: 'html',
                    success: function (html) {
                        $('#collapse-payment-address .panel-body').html(html);

                        $('#collapse-payment-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_address; ?> <i class="fa fa-caret-down"></i></a>');
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    }
                });
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

// Payment Address
$(document).delegate('#button-payment-address', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/payment_address/save',
        type: 'post',
        data: $('#collapse-payment-address input[type=\'text\'], #collapse-payment-address input[type=\'date\'], #collapse-payment-address input[type=\'datetime-local\'], #collapse-payment-address input[type=\'time\'], #collapse-payment-address input[type=\'password\'], #collapse-payment-address input[type=\'checkbox\']:checked, #collapse-payment-address input[type=\'radio\']:checked, #collapse-payment-address input[type=\'hidden\'], #collapse-payment-address textarea, #collapse-payment-address select'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-payment-address').button('loading');
        },
        complete: function () {
            $('#button-payment-address').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                if (json['error']['warning']) {
                    $('#collapse-payment-address .panel-body').prepend('<div class="alert alert-warning">' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                }

                for (i in json['error']) {
                    var element = $('#input-payment-' + i.replace('_', '-'));

                    if ($(element).parent().hasClass('input-group')) {
                        $(element).parent().after('<div class="text-danger">' + json['error'][i] + '</div>');
                    } else {
                        $(element).after('<div class="text-danger">' + json['error'][i] + '</div>');
                    }
                }

                // Highlight any found errors
                $('.text-danger').parent().parent().addClass('has-error');
            } else {
            <
                ? php
                if ($shipping_required) {
                    ?
                >
                    $.ajax({
                        url: 'index.php?route=checkout/shipping_address',
                        dataType: 'html',
                        success: function (html) {
                            $('#collapse-shipping-address .panel-body').html(html);

                            $('#collapse-shipping-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_address; ?> <i class="fa fa-caret-down"></i></a>');

                            $('a[href=\'#collapse-shipping-address\']').trigger('click');

                            $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_shipping_method; ?>');
                            $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_method; ?>');
                            $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                        }
                    });
                <
                    ? php
                } else {
                    ?
                >
                    $.ajax({
                        url: 'index.php?route=checkout/payment_method',
                        dataType: 'html',
                        success: function (html) {
                            $('#collapse-payment-method .panel-body').html(html);

                            $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_method; ?> <i class="fa fa-caret-down"></i></a>');

                            $('a[href=\'#collapse-payment-method\']').trigger('click');

                            $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                        }
                    });
                <
                    ? php
                }
                ?
            >

                $.ajax({
                    url: 'index.php?route=checkout/payment_address',
                    dataType: 'html',
                    success: function (html) {
                        $('#collapse-payment-address .panel-body').html(html);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    }
                });
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

// Shipping Address
$(document).delegate('#button-shipping-address', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/shipping_address/save',
        type: 'post',
        data: $('#collapse-shipping-address input[type=\'text\'], #collapse-shipping-address input[type=\'date\'], #collapse-shipping-address input[type=\'datetime-local\'], #collapse-shipping-address input[type=\'time\'], #collapse-shipping-address input[type=\'password\'], #collapse-shipping-address input[type=\'checkbox\']:checked, #collapse-shipping-address input[type=\'radio\']:checked, #collapse-shipping-address textarea, #collapse-shipping-address select'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-shipping-address').button('loading');
        },
        complete: function () {
            $('#button-shipping-address').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                if (json['error']['warning']) {
                    $('#collapse-shipping-address .panel-body').prepend('<div class="alert alert-warning">' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                }

                for (i in json['error']) {
                    var element = $('#input-shipping-' + i.replace('_', '-'));

                    if ($(element).parent().hasClass('input-group')) {
                        $(element).parent().after('<div class="text-danger">' + json['error'][i] + '</div>');
                    } else {
                        $(element).after('<div class="text-danger">' + json['error'][i] + '</div>');
                    }
                }

                // Highlight any found errors
                $('.text-danger').parent().parent().addClass('has-error');
            } else {
                $.ajax({
                    url: 'index.php?route=checkout/shipping_method',
                    dataType: 'html',
                    success: function (html) {
                        $('#collapse-shipping-method .panel-body').html(html);

                        $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_method; ?> <i class="fa fa-caret-down"></i></a>');

                        $('a[href=\'#collapse-shipping-method\']').trigger('click');

                        $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_method; ?>');
                        $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');

                        $.ajax({
                            url: 'index.php?route=checkout/shipping_address',
                            dataType: 'html',
                            success: function (html) {
                                $('#collapse-shipping-address .panel-body').html(html);
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                            }
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    }
                });

                $.ajax({
                    url: 'index.php?route=checkout/payment_address',
                    dataType: 'html',
                    success: function (html) {
                        $('#collapse-payment-address .panel-body').html(html);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    }
                });
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

// Guest
$(document).delegate('#button-guest', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/guest/save',
        type: 'post',
        data: $('#collapse-payment-address input[type=\'text\'], #collapse-payment-address input[type=\'date\'], #collapse-payment-address input[type=\'datetime-local\'], #collapse-payment-address input[type=\'time\'], #collapse-payment-address input[type=\'checkbox\']:checked, #collapse-payment-address input[type=\'radio\']:checked, #collapse-payment-address input[type=\'hidden\'], #collapse-payment-address textarea, #collapse-payment-address select'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-guest').button('loading');
        },
        complete: function () {
            $('#button-guest').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                if (json['error']['warning']) {
                    $('#collapse-payment-address .panel-body').prepend('<div class="alert alert-warning">' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                }

                for (i in json['error']) {
                    var element = $('#input-payment-' + i.replace('_', '-'));

                    if ($(element).parent().hasClass('input-group')) {
                        $(element).parent().after('<div class="text-danger">' + json['error'][i] + '</div>');
                    } else {
                        $(element).after('<div class="text-danger">' + json['error'][i] + '</div>');
                    }
                }

                // Highlight any found errors
                $('.text-danger').parent().addClass('has-error');
            } else {
            <
                ? php
                if ($shipping_required) {
                    ?
                >
                    var shipping_address = $('#collapse-payment-address input[name=\'shipping_address\']:checked').prop('value');

                    if (shipping_address) {
                        $.ajax({
                            url: 'index.php?route=checkout/shipping_method',
                            dataType: 'html',
                            success: function (html) {
                                // Add the shipping address
                                $.ajax({
                                    url: 'index.php?route=checkout/guest_shipping',
                                    dataType: 'html',
                                    success: function (html) {
                                        $('#collapse-shipping-address .panel-body').html(html);

                                        $('#collapse-shipping-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_address; ?> <i class="fa fa-caret-down"></i></a>');
                                    },
                                    error: function (xhr, ajaxOptions, thrownError) {
                                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                                    }
                                });

                                $('#collapse-shipping-method .panel-body').html(html);

                                $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_method; ?> <i class="fa fa-caret-down"></i></a>');

                                $('a[href=\'#collapse-shipping-method\']').trigger('click');

                                $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_method; ?>');
                                $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                            }
                        });
                    } else {
                        $.ajax({
                            url: 'index.php?route=checkout/guest_shipping',
                            dataType: 'html',
                            success: function (html) {
                                $('#collapse-shipping-address .panel-body').html(html);

                                $('#collapse-shipping-address').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-address" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_address; ?> <i class="fa fa-caret-down"></i></a>');

                                $('a[href=\'#collapse-shipping-address\']').trigger('click');

                                $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_shipping_method; ?>');
                                $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_method; ?>');
                                $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                            }
                        });
                    }
                <
                    ? php
                } else {
                    ?
                >
                    $.ajax({
                        url: 'index.php?route=checkout/payment_method',
                        dataType: 'html',
                        success: function (html) {
                            $('#collapse-payment-method .panel-body').html(html);

                            $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_method; ?> <i class="fa fa-caret-down"></i></a>');

                            $('a[href=\'#collapse-payment-method\']').trigger('click');

                            $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                        }
                    });
                <
                    ? php
                }
                ?
            >
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

// Guest Shipping
$(document).delegate('#button-guest-shipping', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/guest_shipping/save',
        type: 'post',
        data: $('#collapse-shipping-address input[type=\'text\'], #collapse-shipping-address input[type=\'date\'], #collapse-shipping-address input[type=\'datetime-local\'], #collapse-shipping-address input[type=\'time\'], #collapse-shipping-address input[type=\'password\'], #collapse-shipping-address input[type=\'checkbox\']:checked, #collapse-shipping-address input[type=\'radio\']:checked, #collapse-shipping-address textarea, #collapse-shipping-address select'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-guest-shipping').button('loading');
        },
        complete: function () {
            $('#button-guest-shipping').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                if (json['error']['warning']) {
                    $('#collapse-shipping-address .panel-body').prepend('<div class="alert alert-warning">' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                }

                for (i in json['error']) {
                    var element = $('#input-shipping-' + i.replace('_', '-'));

                    if ($(element).parent().hasClass('input-group')) {
                        $(element).parent().after('<div class="text-danger">' + json['error'][i] + '</div>');
                    } else {
                        $(element).after('<div class="text-danger">' + json['error'][i] + '</div>');
                    }
                }

                // Highlight any found errors
                $('.text-danger').parent().addClass('has-error');
            } else {
                $.ajax({
                    url: 'index.php?route=checkout/shipping_method',
                    dataType: 'html',
                    success: function (html) {
                        $('#collapse-shipping-method .panel-body').html(html);

                        $('#collapse-shipping-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-shipping-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_shipping_method; ?> <i class="fa fa-caret-down"></i>');

                        $('a[href=\'#collapse-shipping-method\']').trigger('click');

                        $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_payment_method; ?>');
                        $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    }
                });
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

$(document).delegate('#button-shipping-method', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/shipping_method/save',
        type: 'post',
        data: $('#collapse-shipping-method input[type=\'radio\']:checked, #collapse-shipping-method textarea'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-shipping-method').button('loading');
        },
        complete: function () {
            $('#button-shipping-method').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                if (json['error']['warning']) {
                    $('#collapse-shipping-method .panel-body').prepend('<div class="alert alert-warning">' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                }
            } else {
                $.ajax({
                    url: 'index.php?route=checkout/payment_method',
                    dataType: 'html',
                    success: function (html) {
                        $('#collapse-payment-method .panel-body').html(html);

                        $('#collapse-payment-method').parent().find('.panel-heading .panel-title').html('<a href="#collapse-payment-method" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_payment_method; ?> <i class="fa fa-caret-down"></i></a>');

                        $('a[href=\'#collapse-payment-method\']').trigger('click');

                        $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<?php echo $text_checkout_confirm; ?>');
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    }
                });
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});

$(document).delegate('#button-payment-method', 'click', function () {
    $.ajax({
        url: 'index.php?route=checkout/payment_method/save',
        type: 'post',
        data: $('#collapse-payment-method input[type=\'radio\']:checked, #collapse-payment-method input[type=\'checkbox\']:checked, #collapse-payment-method textarea'),
        dataType: 'json',
        beforeSend: function () {
            $('#button-payment-method').button('loading');
        },
        complete: function () {
            $('#button-payment-method').button('reset');
        },
        success: function (json) {
            $('.alert, .text-danger').remove();

            if (json['redirect']) {
                location = json['redirect'];
            } else if (json['error']) {
                if (json['error']['warning']) {
                    $('#collapse-payment-method .panel-body').prepend('<div class="alert alert-warning">' + json['error']['warning'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
                }
            } else {
                $.ajax({
                    url: 'index.php?route=checkout/confirm',
                    dataType: 'html',
                    success: function (html) {
                        $('#collapse-checkout-confirm .panel-body').html(html);

                        $('#collapse-checkout-confirm').parent().find('.panel-heading .panel-title').html('<a href="#collapse-checkout-confirm" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle"><?php echo $text_checkout_confirm; ?> <i class="fa fa-caret-down"></i></a>');

                        $('a[href=\'#collapse-checkout-confirm\']').trigger('click');
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    }
                });
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
        }
    });
});
//--></script>
<?php echo $footer; ?>