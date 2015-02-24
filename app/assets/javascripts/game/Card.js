define(["game/Rank", "game/Suit"], function (Rank, Suit) {
  "use strict";

  function Card(game, id, rank, suit, faceUp) {
    this.id = id;
    this.rank = Rank.fromChar(rank);
    this.suit = Suit.fromChar(suit);
    this.name = rank + suit + ": " + id;
    this.faceUp = faceUp;

    this.animation = null;

    this.spriteIndex = (this.suit.index * 13) + (this.rank.value - 2);
    if(this.faceUp) {
      Phaser.Sprite.call(this, game, 0, 0, 'card-medium', this.spriteIndex);
    } else {
      Phaser.Sprite.call(this, game, 0, 0, 'card-back-medium');
    }

    this.inputEnabled = true;
    this.dragging = false;

    this.game.addCard(this);

    this.events.onInputDown.add(this.onInputDown, this);
    this.events.onInputUp.add(this.onInputUp, this);
  }

  Card.prototype = Object.create(Phaser.Sprite.prototype);
  Card.prototype.constructor = Card;

  Card.prototype.onInputDown = function(e, p) {
    this.pile.startDrag(this, p);
  };

  Card.prototype.startDrag = function(p, offset) {
    this.dragging = true;
    this.inputOriginalPosition = this.position.clone();
    this.anchorPointX = ((p.positionDown.x - this.game.playmat.x) / this.game.playmat.scale.x) - this.x;
    this.anchorPointY = ((p.positionDown.y - this.game.playmat.y) / this.game.playmat.scale.y) - this.y;
    //console.log("InputDown", p.positionDown.x, " - ", this.game.playmat.x, " - (", this.x, "*", this.game.playmat.scale.x, " = ", (this.x * this.game.playmat.scale.x), ") = ", this.anchorPointX);
    this.bringToTop();
  };

  Card.prototype.onInputUp = function(e, p) {
    if(this.dragging) {
      this.pile.endDrag(p);
    } else {
      this.pile.cardSelected(this);
    }
  };

  Card.prototype.cancelDrag = function(p) {
    if(this.dragging) {
      this.dragging = false;
      var xDelta = Math.abs(p.positionDown.x - p.positionUp.x);
      var yDelta = Math.abs(p.positionDown.y - p.positionUp.y);
      if(xDelta > 0 || yDelta > 0) {
        // Dragged
        this.game.add.tween(this).to({x: this.inputOriginalPosition.x, y: this.inputOriginalPosition.y}, 500, Phaser.Easing.Quadratic.InOut, true);
      } else {
        this.pile.cardSelected(this, p);
      }
    } else {
      this.pile.cardSelected(this, p);
    }
  };

  Card.prototype.toString = function() {
    return this.rank.char + this.suit.char + ": " + this.id.substring(0, 8);
  };

  Card.prototype.update = function() {
    if(this.animation === null) {
      if(this.dragging) {
        this.x = ((this.game.input.x - this.game.playmat.x) / this.game.playmat.scale.x) - (this.anchorPointX);
        this.y = ((this.game.input.y - this.game.playmat.y) / this.game.playmat.scale.y) - (this.anchorPointY);
      }
    } else if(this.animation.id === "mouse") {
      var tgtX = this.game.input.x - ((this.width / 2) * this.game.playmat.scale.x);
      var tgtY = this.game.input.y - ((this.height / 2) * this.game.playmat.scale.y);
      var distance = this.game.math.distance(this.world.x, this.world.y, tgtX, tgtY);
      if(distance > 20) {
        var rotation = this.game.math.angleBetween(this.world.x, this.world.y, tgtX, tgtY);
        this.x += Math.cos(rotation) * 10;
        this.y += Math.sin(rotation) * 10;
      }
    }
  };

  return Card;
});