package com.segdx.game.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Gossip;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.entity.TradePost;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.states.GameState;

public class GossipManager {
	
	private Array<SpaceNode> restnodes;
	private Array<SpaceNode> tradenodes;
	
	private Array<SpaceNode> eventnodes;
	
	
	public GossipManager(SpaceMap map) {
		restnodes = new Array<SpaceNode>();
		tradenodes = new Array<SpaceNode>();
		
		eventnodes = new Array<SpaceNode>();
		
		restnodes.addAll(map.getRestnodes());
		tradenodes.addAll(map.getTradenodes());
		eventnodes.addAll(map.getPassivenodes());
		
		
	}
	
	public Gossip getTradeGossip(){
		Gossip g = null;
		Array<SpaceNode> trades = new Array<SpaceNode>();
		for (int i = 0; i < tradenodes.size; i++) {
			TradePost post = tradenodes.get(i).getTradepost();
			if(post.hasEventModifier()){
				trades.add(tradenodes.get(i));
			}
		}
		int rand = 0;
		g = new Gossip();
		if(trades.size>0){
			rand = NodeEvent.getRandomInt(0,trades.size-1);
			g.setNode(trades.get(rand));
		}else{
			g.setNode(tradenodes.get(NodeEvent.getRandomInt(0,tradenodes.size-1)));
			
		}	
		g.setName("Great Deals!");
		g.setCost(Gossip.TRADE_GOSSIP);
		
		return g;
		
	}
	
	public Gossip getWorkGossip(){
		Gossip g = null;
		Array<SpaceNode> worknodes = new Array<SpaceNode>();
		for (int i = 0; i < restnodes.size; i++) {
			if(restnodes.get(i).getReststop().getWork()!=null){
				worknodes.add(restnodes.get(i));
			}
		}
		int rand = NodeEvent.getRandomInt(0,worknodes.size-1);
		g = new Gossip();
		g.setName(worknodes.get(rand).getReststop().getWork().getName());
		g.setNode(worknodes.get(rand));
		g.setCost(Gossip.WORK_GOSSIP);
		g.setDescription(worknodes.get(rand).getReststop().getWork().getDesc());
		return g;
	}
	
	public Gossip getEventGossip(){
		Gossip g = null;
		Array<SpaceNode> events = new Array<SpaceNode>();
		for (int i = 0; i < eventnodes.size; i++) {
			if(eventnodes.get(i).getEvent()!=null){
				events.add(eventnodes.get(i));
			}
		}
		int rand = NodeEvent.getRandomInt(0,events.size-1);
		g = new Gossip();
		g.setName("Event Gossip - find out whats happening!");
		g.setDescription(events.get(rand).getEvent().getDescription());
		g.setNode(events.get(rand));
		g.setCost(Gossip.EVENT_GOSSIP);
		return g;
	}
	
	public Table getGossipTable(final GameState state){
		Table t = new Table();
		final Player p = state.getSpaceMap().getPlayer();
		final Gossip eg = getEventGossip();
		TextButton event = new TextButton(eg.getName()+" $"+Gossip.EVENT_GOSSIP, state.skin);
		event.getLabel().setFontScale(.6f);
		if(state.getSpaceMap().getPlayer().getCurrency()<eg.getCost()){
			event.setDisabled(true);
			event.setColor(Color.FIREBRICK);
		}else{
			event.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y) {
					p.setCurrency(p.getCurrency()-eg.getCost());
					ShipAbility.showMessage(eg.getName()+"            ", 
							eg.getDescription(), state.skin).show(state.uistage);
					eg.tweenTo(state);
					state.updateRestBar();
				}
			});
		}
		final Gossip tg = getTradeGossip();
		TextButton trade = new TextButton(tg.getName()+" $"+Gossip.TRADE_GOSSIP, state.skin);
		trade.getLabel().setFontScale(.6f);
		if(state.getSpaceMap().getPlayer().getCurrency()<tg.getCost()){
			trade.setDisabled(true);
			trade.setColor(Color.FIREBRICK);
		}else{
			trade.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y) {
					p.setCurrency(p.getCurrency()-tg.getCost());
					tg.tweenTo(state);
					state.updateRestBar();
				}
			});
		}
		final Gossip wg = getWorkGossip();
		TextButton work = new TextButton(wg.getName()+" $"+Gossip.WORK_GOSSIP, state.skin);
		work.getLabel().setFontScale(.6f);
		if(state.getSpaceMap().getPlayer().getCurrency()<wg.getCost()){
			work.setDisabled(true);
			work.setColor(Color.FIREBRICK);
		}else{
			work.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y) {
					p.setCurrency(p.getCurrency()-wg.getCost());
					ShipAbility.showMessage(wg.getName()+"            ", 
							wg.getDescription(), state.skin).show(state.uistage);
					wg.tweenTo(state);
					state.updateRestBar();
				}
			});
		}
		t.add(event).left().expand().row();
		t.add(trade).left().expand().row();
		t.add(work).left().expand().row();
		
		
		return t;
	}
	
	//createNewgossip
	//------add gossip to rest stops that need gossip
	
	
	//removeGossup
	//-------remove useless gossip from rest stops that have said gossip
	

}
